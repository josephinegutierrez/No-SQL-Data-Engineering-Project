package es.um.nosql.s13e.entitydifferentiation.m2t.morphia

import es.um.nosql.s13e.entitydifferentiation.EntityDifferentiation.EntityDiff
import es.um.nosql.s13e.entitydifferentiation.EntityDifferentiation.EntityDifferentiation
import es.um.nosql.s13e.entitydifferentiation.EntityDifferentiation.EntityDifferentiationPackage
import es.um.nosql.s13e.entitydifferentiation.EntityDifferentiation.PropertySpec
import es.um.nosql.s13e.NoSQLSchema.Aggregate
import es.um.nosql.s13e.NoSQLSchema.Association
import es.um.nosql.s13e.NoSQLSchema.Attribute
import es.um.nosql.s13e.NoSQLSchema.EntityType
import es.um.nosql.s13e.NoSQLSchema.PTuple
import es.um.nosql.s13e.NoSQLSchema.PrimitiveType
import es.um.nosql.s13e.NoSQLSchema.Property
import es.um.nosql.s13e.NoSQLSchema.Reference
import es.um.nosql.s13e.entitydifferentiation.m2t.commons.Commons
import es.um.nosql.s13e.entitydifferentiation.m2t.commons.DependencyAnalyzer
import es.um.nosql.s13e.entitydifferentiation.m2t.config.ConfigMorphia
import es.um.nosql.s13e.util.ModelLoader
import java.io.File
import java.util.ArrayList
import java.util.Comparator
import java.util.List
import java.util.stream.IntStream
import es.um.nosql.s13e.NoSQLSchema.PList
import es.um.nosql.s13e.NoSQLSchema.PSet

/**
 * Class designed to perform the Morphia code generation: Java
 */
class DiffToMorphia
{
  /**
   * This route stores the import routes between objects.
   */
  var importRoute = "";

  DependencyAnalyzer analyzer;

  MorphiaIndexValGen indexValGen;

  /**
   * Method used to start the generation process from a diff model file
   */
  def void m2t(File modelFile, File outputFolder, File configFile)
  {
    val loader = new ModelLoader(EntityDifferentiationPackage.eINSTANCE);
    val diff = loader.load(modelFile, EntityDifferentiation);

    m2t(diff, outputFolder, configFile);
  }

  /**
   * Method used to start the generation process from an EntityDifferentiation object.
   */
  def void m2t(EntityDifferentiation diff, File outputFolder, File configFile)
  {
    val modelName = diff.name;
    val outputDir = outputFolder.toPath.resolve(modelName).toFile;
    outputDir.mkdirs;
    if (outputDir.toString.contains("src"))
    {
    	// TODO: Write this OS-independent
      importRoute = outputDir.toString.substring(outputDir.toString.lastIndexOf("src") + 4).replace(File.separatorChar, ".");
    }
    else
    {
      importRoute = outputDir.toString;
    }

    // Process the configuration file
    indexValGen = new MorphiaIndexValGen(Commons.PARSE_CONFIG_FILE(ConfigMorphia, configFile, diff));

    // Calc dependencies between entities
    analyzer = new DependencyAnalyzer();
    analyzer.performAnalysis(diff);
    analyzer.getTopOrderEntities().forEach[e | Commons.WRITE_TO_FILE(outputDir, schemaFileName(e), genSchema(e))];
  }

  private def schemaFileName(EntityType entity)
  {
    entity.name + ".java";
  }

  /**
   * This method generates the basic structure of the Java class.
   */
  private def genSchema(EntityType entity)
  '''
    package ??importRoute??;

    ??genIncludes(entity)??

    ??IF entity.isRoot??@Entity(value = "??entity.name.toFirstLower??", noClassnameStored = true)??ELSE??@Embedded??ENDIF??
    ??indexValGen.genIndexesForEntity(entity)??
    public class ??entity.name??
    {
      ??genSpecs(entity, analyzer.getDiffByEntity().get(entity))??
    }
  '''

  /**
   * To generate imports, we just check the conditions in which these imports will be used.
   */
  private def genIncludes(EntityType entity)
  {
    val collListUnionProperties = analyzer.getTypeListByPropertyName.get(entity).values;
    '''
    ??IF entity.isRoot??
    import org.mongodb.morphia.annotations.Entity;
    import org.mongodb.morphia.annotations.Id;
    ??IF entityReferencesObjectId(entity)??
    import org.bson.types.ObjectId;
    ??ENDIF??
    ??ENDIF??
    ??IF collListUnionProperties.exists[l | !l.isEmpty]??
      ??IF collListUnionProperties.exists[l | l.exists[ps | ps.property instanceof Aggregate]]??
      import ??importRoute??.commons.Commons;
      ??ENDIF??
      import org.mongodb.morphia.annotations.PreLoad;
      import org.mongodb.morphia.annotations.PreSave;
      import com.mongodb.DBObject;
    ??ENDIF??
    ??IF !entity.isRoot || entity.variations.exists[ev | ev.properties.exists[p | p instanceof Aggregate]]??import org.mongodb.morphia.annotations.Embedded;??ENDIF??
    ??IF entity.variations.exists[ev | ev.properties.exists[p | p instanceof Attribute]]??import org.mongodb.morphia.annotations.Property;??ENDIF??
    ??IF entity.variations.exists[ev | ev.properties.exists[p | p instanceof Reference]]??import org.mongodb.morphia.annotations.Reference;??ENDIF??
    ??IF !analyzer.getDiffByEntity().get(entity).commonProps.isEmpty??import javax.validation.constraints.NotNull;??ENDIF??
    ??IF entityHasMultipleCardinalityAttributes(entity)??
    import java.util.List;
    ??ENDIF??
    ??indexValGen.genIncludesForEntity(entity)??

    ??FOR EntityType e : analyzer.getEntityDeps().get(entity).sortWith(Comparator.comparing[e | analyzer.getTopOrderEntities().indexOf(e)])??
      import ??importRoute??.??e.name??;
    ??ENDFOR??
    '''
  }

  protected def boolean entityHasMultipleCardinalityAttributes(EntityType entity)
  {
    entity.variations.exists[ev | ev.properties.exists[p | (p instanceof Association &&
          ((p as Association).lowerBound !== 1 || (p as Association).upperBound !== 1))
          || (p instanceof Attribute && ((p as Attribute).type instanceof PTuple || (p as Attribute).type instanceof PList || (p as Attribute).type instanceof PSet))]]
  }

  protected def boolean entityReferencesObjectId(EntityType entity) 
  {
    entity.variations.exists[ev | ev.properties.exists[p | (p instanceof Attribute && (p as Attribute).type instanceof PrimitiveType && ((p as Attribute).type as PrimitiveType).name.equals("ObjectId")) ||
      (p instanceof Reference && (p as Reference).originalType.equals("ObjectId"))]]
  }

  /**
   * For each property of any variation of an entity, generate code.
   * Two methods are called depending on if there is a need for the type check.
   * s.key stores a PropertySpec
   * s.value stores "required" or not
   */
  private def genSpecs(EntityType entity, EntityDiff spec)
  '''
    ??FOR s : (spec.commonProps.map[cp | cp -> true] + spec.specificProps.map[sp | sp -> false])
    	.reject[p | p.key.property.name.startsWith("_") && !p.key.property.name.equals("_id")]
      	.sortBy[p | p.key.property.name] SEPARATOR '\n'??
      ??IF s.key.needsTypeCheck??
        ??genCodeForTypeCheckProperty(entity, s.key.property, s.value)??
      ??ELSE??
        ??genCodeForProperty(s.key.property, s.value)??
      ??ENDIF??
    ??ENDFOR??
  '''

  private def specificProps(EntityDiff spec)
  {
    spec.variationDiffs.map[propertySpecs].fold(<PropertySpec>newHashSet(),
      [result, neew |
        val names = result.map[p | p.property.name].toSet
        result.addAll(neew.filter[p | !names.contains(p.property.name)])
        result
      ])
  }

  /**
   * Method used to try to reduce a Property Union to a single property.
   * This is sometimes possible, for example when a Union is composed of a Reference String[] and a Tuple String[].
   * If the reduction is possible, we generate the property as any other.
   * If not, a Union is generated.
   */
  private def genCodeForTypeCheckProperty(EntityType entity, Property property, boolean required)
  {
    val typeList = analyzer.getTypeListByPropertyName().get(entity).get(property.name)
    // On uniqueTypeList we removed duplicated property types, such as a String PrimitiveType and a Reference w originalType String.
    val uniqueTypeList = new ArrayList<Property>();
    // Just a shortcut list so we don't have to access every time to the type field of a property (and all its casts...)
    val typeShortcutList = new ArrayList<String>();

    // We try to reduce Unions. For example, a Union of type Reference.String and a PrimitiveType.String should be reduced to a String field.
    for (PropertySpec ps : typeList)
      addToReduceLists(ps.property, genTypeForUnion(ps.property).toString, uniqueTypeList, typeShortcutList);

    // We reduced the union to a single type!
    if (uniqueTypeList.size == 1)
    {
      genCodeForProperty(uniqueTypeList.head, required);
    }
    else
    {
      genUnion(uniqueTypeList, required);
    }
  }

  private def addToReduceLists(Property p, String name, List<Property> uniqueTypeList, List<String> typeShortcutList)
  {
    if (!typeShortcutList.exists[type | type.equals(name)])
    {
      uniqueTypeList.add(p);
      typeShortcutList.add(name);
    }
  }
  /** End of the Union reduction process */

  /** Starting the Union code */

  /**
   * Method used to generate Union code. In Java this is performed by creating several private attributesan Object attribute
   * and some restrictions when setting that attribute.
   * It is also neccesary to create a @Preload method in order to identify the Union during the loading process.
   */
  private def String genUnion(Iterable<Property> list, boolean required)
  {
    val theTypes = list.map[p | genTypeForUnion(p)];
    val theName = list.head.name;
    val theSignature = theTypes.join('_').replace("<", "").replace(">", "");
    var numProperties = IntStream.range(0, list.length).toArray;

  '''
  ??FOR int iterator : numProperties??
  private ??genTypeForUnion(list.get(iterator))?? __??list.get(iterator).name????iterator + 1??;
  ??ENDFOR??

  // @Union_??theTypes.join('_')??
  ??IF required??@NotNull(message = "??theName?? can't be null")??ENDIF??
  ??indexValGen.genValidatorsForField(list.head.eContainer.eContainer as EntityType, theName)??
  @SuppressWarnings("unused")
  private Object ??theName??;
  public Object get??theName.toFirstUpper??()
  {
    ??FOR int iterator : numProperties??
    if (__??list.get(iterator).name????iterator + 1?? != null) return __??list.get(iterator).name????iterator + 1??;
    ??ENDFOR??
    return null;
  }

  public void set??theName.toFirstUpper??(Object ??theName??)
  {
    ??FOR int iterator : numProperties SEPARATOR "\nelse"??
    if (??genUnionCompareMethod(list.get(iterator), list.get(iterator).name, false)??)
    {
      this.__??theName????iterator + 1?? = ??genUnionAssignMethod(list.get(iterator), list.get(iterator).name, false)??;
      ??FOR int iterator2 : numProperties??
        ??IF (iterator !== iterator2)??
          this.__??theName????iterator2 + 1?? = null;
        ??ENDIF??
      ??ENDFOR??
      this.??theName?? = ??theName??;
    }??ENDFOR??
    else
      throw new ClassCastException("??theName?? must be of type ??theTypes.join(' or ')??");
  }

  @PreLoad
  private void preLoadUnion_??theSignature??(DBObject dbObj)
  {
    ??IF !required??
    if (!dbObj.containsField("??theName??"))
      return;
    ??ENDIF??

    Object fieldObj = dbObj.get("??theName??");

    ??FOR int iterator : numProperties SEPARATOR "\nelse "??
    if (??genUnionCompareMethod(list.get(iterator), "fieldObj", true)??)
      this.__??list.get(iterator).name????iterator + 1?? = ??genUnionAssignMethod(list.get(iterator), "fieldObj", true)??;
    ??ENDFOR??
    else
      throw new ClassCastException("??theName?? must be of type ??theTypes.join(' or ')??");

    dbObj.removeField("??theName??");
  }

  @PreSave
  private void preSaveUnion_??theSignature??(DBObject dbObj)
  {
    ??FOR int iterator : numProperties SEPARATOR "\nelse"??
    if (__??list.get(iterator).name????iterator + 1?? != null)
    {
      dbObj.put("??list.get(iterator).name??", dbObj.get("__??list.get(iterator).name????iterator + 1??"));
      dbObj.removeField("__??list.get(iterator).name????iterator + 1??");
    }??ENDFOR??
  }
  '''
  }

  private def genTypeForUnion(Property p)
  {
    if (p instanceof Reference)
    {
      var ref = p as Reference;
      val result = genAttributeType(ref.originalType);
      if (ref.lowerBound === 1 && ref.upperBound === 1)
        { return result; }
      else
        { return "List<" + result + ">"; }
    }
    else
      genTypeForProperty(p);
  }

  private def dispatch genUnionCompareMethod(Aggregate aggr, String name, boolean preload)
  {
    if (aggr.lowerBound === 1 && aggr.upperBound === 1)
    '''??name?? instanceof ??genTypeForProperty(aggr)??'''
    else
    '''Commons.IS_CASTABLE_LIST??IF preload??_OBJDB??ENDIF??(??genTypeForProperty(aggr).toString.replace("List<", "").replace(">", "")??.class, ??name??)'''
  }

  private def dispatch genUnionCompareMethod(Reference ref, String name, boolean preload)
  {
    if (ref.lowerBound === 1 && ref.upperBound === 1)
    '''??name?? instanceof ??genAttributeType(ref.originalType)??'''
    else
    '''Commons.IS_CASTABLE_LIST(??genAttributeType(ref.originalType)??.class, ??name??)'''
  }

  private def dispatch genUnionCompareMethod(Attribute attr, String name, boolean preload)
  {
    if (attr.type instanceof PrimitiveType)
    '''??name?? instanceof ??genTypeForProperty(attr)??'''
    else
    '''Commons.IS_CASTABLE_LIST(??genTypeForProperty(attr).toString.replace("List<", "").replace(">", "")??.class, ??name??)'''
  }

  private def dispatch genUnionAssignMethod(Aggregate aggr, String name, boolean preload)
  {
    if (aggr.lowerBound === 1 && aggr.upperBound === 1)
    '''(??genTypeForProperty(aggr)??)??name??'''
    else
    '''Commons.CAST_LIST??IF preload??_OBJDB??ENDIF??(??genTypeForProperty(aggr).toString.replace("List<", "").replace(">", "")??.class, ??name??)'''
  }

  private def dispatch genUnionAssignMethod(Reference ref, String name, boolean preload)
  {
    if (ref.lowerBound === 1 && ref.upperBound === 1)
    '''(??genAttributeType(ref.originalType)??)??name??'''
    else
    '''Commons.CAST_LIST(??genAttributeType(ref.originalType).toString??.class, ??name??)'''
  }

  private def dispatch genUnionAssignMethod(Attribute attr, String name, boolean preload)
  {
    if (attr.type instanceof PrimitiveType)
    '''(??genTypeForProperty(attr)??)??name??'''
    else
    '''Commons.CAST_LIST(??genTypeForProperty(attr).toString.replace("List<", "").replace(">", "")??.class, ??name??)'''
  }
  /** Ending the Union code */

  /**
   * Generate code method for Aggregation
   * It generates a private attribute, and Get/Set methods.
   */
  private def dispatch genCodeForProperty(Aggregate aggr, boolean required)
  '''
    @Embedded
    ??IF required??@NotNull(message = "??aggr.name?? can't be null")??ENDIF??
    ??indexValGen.genValidatorsForField(aggr.eContainer.eContainer as EntityType, aggr.name)??
    private ??genTypeForProperty(aggr)?? ??aggr.name??;
    public ??genTypeForProperty(aggr)?? get??aggr.name.toFirstUpper??() {return this.??aggr.name??;}
    public void set??aggr.name.toFirstUpper??(??genTypeForProperty(aggr)?? ??aggr.name??) {this.??aggr.name?? = ??aggr.name??;}
  '''

  /**
   * Shortcut method to generate an Aggregate type.
   */
  private def dispatch genTypeForProperty(Aggregate aggr)
  {
    var entityName = (aggr.aggregates.get(0).container as EntityType).name;
    if (aggr.lowerBound !== 1 || aggr.upperBound !== 1)
      entityName = "List<" + entityName + ">";

    entityName;
  }

  /**
   * Generate code method for Reference
   * It generates a private attribute, and Get/Set methods.
   */
  private def dispatch genCodeForProperty(Reference ref, boolean required)
  '''
    @Reference??IF !Commons.IS_DBREF(ref)??(idOnly = true??indexValGen.genPopulateReferences(ref.eContainer.eContainer as EntityType, ref.name)??)??ENDIF??
    ??IF required??@NotNull(message = "??ref.name?? can't be null")??ENDIF??
    ??indexValGen.genValidatorsForField(ref.eContainer.eContainer as EntityType, ref.name)??
    private ??genTypeForProperty(ref)?? ??ref.name??;
    public ??genTypeForProperty(ref)?? get??ref.name.toFirstUpper??() {return this.??ref.name??;}
    public void set??ref.name.toFirstUpper??(??genTypeForProperty(ref)?? ??ref.name??) {this.??ref.name?? = ??ref.name??;}
  '''

  /**
   * Shortcut method to generate a Reference type.
   */
  private def dispatch genTypeForProperty(Reference ref)
  {
    var returnValue = ref.refsTo.name;

    if (ref.lowerBound !== 1 || ref.upperBound !== 1)
      returnValue = "List<" + returnValue + ">";

    returnValue;
  }

  /**
   * Generate code method for Attribute
   * It generates a private attribute, and Get/Set methods.
   */
  private def dispatch genCodeForProperty(Attribute a, boolean required)
  '''
    ??IF a.name.toLowerCase.equals("_id")??@Id??ELSE??@Property??ENDIF??
    ??IF required??@NotNull(message = "??a.name?? can't be null")??ENDIF??
    ??indexValGen.genValidatorsForField(a.eContainer.eContainer as EntityType, a.name)??
    private ??genTypeForProperty(a)?? ??a.name??;
    public ??genTypeForProperty(a)?? get??a.name.toFirstUpper??() {return this.??a.name??;}
    public void set??a.name.toFirstUpper??(??genTypeForProperty(a)?? ??a.name??) {this.??a.name?? = ??a.name??;}
  '''

  /**
   * Shortcut method to generate an Attribute type.
   */
  private def dispatch genTypeForProperty(Attribute a)
  {
    genAttributeType(a.type)
  }

  /**
   * Shortcut method to generate a PrimitiveType type.
   */
  private def dispatch CharSequence genAttributeType(PrimitiveType type)
  {
    genAttributeType(type.name)
  }

  /**
   * Shortcut method to generate a Tuple type.
   */
  // TODO: Treat the PMap metaclass.
  private def dispatch CharSequence genAttributeType(PTuple tuple)
  {
    if (tuple.elements.size == 1)
      '''List<??genAttributeType(tuple.elements.get(0))??>'''
    else
      // Heterogeneous arrays. Too complex for now...
      '''List<Object>'''
  }

  /**
   * Shortcut method to generate a PList type
   */
  private def dispatch CharSequence genAttributeType(PList list)
    '''List<??genAttributeType(list.elementType)??>'''

  /**
   * Shortcut method to generate a PSet type
   */
  private def dispatch CharSequence genAttributeType(PSet set)
    '''List<??genAttributeType(set.elementType)??>'''

  private def dispatch CharSequence genAttributeType(String type)
  {
    switch typeName : type.toLowerCase
    {
      case Commons.IS_STRING(typeName) : "String"
      case Commons.IS_INT(typeName) : "Integer"
      case Commons.IS_DOUBLE(typeName) : "Double"
      case Commons.IS_BOOLEAN(typeName) : "Boolean"
      case Commons.IS_OBJECTID(typeName) : "ObjectId"
      default: ""
    }
  }
}
package es.um.nosql.schemainference.m2t.morphia

import java.io.File
import es.um.nosql.schemainference.NoSQLSchema.Entity
import java.util.List
import java.util.Map
import java.util.Set
import es.um.nosql.schemainference.entitydifferentiation.EntityDiffSpec
import es.um.nosql.schemainference.entitydifferentiation.PropertySpec
import es.um.nosql.schemainference.util.emf.ModelLoader
import es.um.nosql.schemainference.entitydifferentiation.EntitydifferentiationPackage
import es.um.nosql.schemainference.entitydifferentiation.EntityDifferentiation
import es.um.nosql.schemainference.NoSQLSchema.Aggregate
import java.io.PrintStream
import java.util.Comparator
import es.um.nosql.schemainference.NoSQLSchema.Attribute
import es.um.nosql.schemainference.NoSQLSchema.Reference
import es.um.nosql.schemainference.NoSQLSchema.Tuple
import java.util.regex.Pattern
import es.um.nosql.schemainference.NoSQLSchema.PrimitiveType
import es.um.nosql.schemainference.NoSQLSchema.Property
import java.util.ArrayList

class DiffToMorphia
{
  var modelName = "";
  var importRoute = "";
  static File outputDir;

  // List of dependencies
  List<Entity> topOrderEntities
  Map<Entity, Set<Entity>> entityDeps
  Map<Entity, Set<Entity>> inverseEntityDeps
  Map<Entity, EntityDiffSpec> diffByEntity
  Map<Entity, Map<String, List<PropertySpec>>> typeListByPropertyName

  /**
   * Method used to start the generation process from a diff model file
   */
  def void m2t(File modelFile, File outputFolder)
  {
    val loader = new ModelLoader(EntitydifferentiationPackage.eINSTANCE);
    val diff = loader.load(modelFile, EntityDifferentiation);

    m2t(diff, outputFolder);
  }

  /**
   * Method used to start the generation process from an EntityDifferentiation object
   */
  def void m2t(EntityDifferentiation diff, File outputFolder)
  {
    modelName = diff.name;
    outputDir = outputFolder.toPath.resolve(modelName).toFile;
    outputDir.mkdirs;
    if (outputDir.toString.contains("src"))
    {
      importRoute = outputDir.toString.substring(outputDir.toString.lastIndexOf("src") + 4).replace(File.separatorChar, ".");
    }
    else
    {
      importRoute = outputDir.toString;
    }

    diffByEntity = newHashMap(diff.entityDiffSpecs.map[ed | ed.entity -> ed])
    val entities = diff.entityDiffSpecs.map[entity]

    // Calc dependencies between entities
    topOrderEntities = calculateDeps(entities)

    typeListByPropertyName = calcTypeListMatrix(entities)
    topOrderEntities.forEach[e | writeToFile(schemaFileName(e), genSchema(e))]
  }

  def schemaFileName(Entity e)
  {
    e.name + ".java"
  }

  def genSchema(Entity e) '''
    package «importRoute»;

    «genIncludes(e)»

    «IF (e.entityversions.exists[ev | ev.isRoot])»@Entity(noClassnameStored = true)«ELSE»@Embedded«ENDIF»
    public class «e.name»
    {
      «IF (e.entityversions.exists[ev | ev.isRoot])»
      @Id
      private ObjectId _id;
      public ObjectId getObjectId() {return this._id;}
      public void setObjectId(ObjectId _id) {this._id = _id;}

      «ENDIF»
      «genSpecs(e, diffByEntity.get(e))»
    }
  '''

  // Actually, Commons should not be imported if there is a Union which is reduced on a single element.
  // Doesnt seem easy to bypass these cases at this point, since unions are analyzed later on.
  def genIncludes(Entity entity) '''
    «IF (entity.entityversions.exists[ev | ev.isRoot])»
      import org.mongodb.morphia.annotations.Entity;
      import org.mongodb.morphia.annotations.Id;
      import org.bson.types.ObjectId;
    «ENDIF»
    «IF (typeListByPropertyName.get(entity).values.exists[l | !l.isEmpty])»
      import «importRoute».commons.Commons;
      import org.mongodb.morphia.annotations.PreLoad;
      import com.mongodb.DBObject;
    «ENDIF»
    «IF (entity.entityversions.exists[ev | !ev.isRoot || ev.properties.exists[p | p instanceof Aggregate]])»import org.mongodb.morphia.annotations.Embedded;«ENDIF»
    «IF (entity.entityversions.exists[ev | !ev.properties.empty])»import org.mongodb.morphia.annotations.Property;«ENDIF»
    «IF (entity.entityversions.exists[ev | ev.properties.exists[p | p instanceof Reference && expandRef(p as Reference).length == 2]])»import org.mongodb.morphia.annotations.Reference;«ENDIF»
    import javax.validation.constraints.NotNull;

    «FOR Entity e : entityDeps.get(entity).sortWith(Comparator.comparing[e | topOrderEntities.indexOf(e)])»
      import «importRoute».«e.name»;
    «ENDFOR»
  '''

  def genSpecs(Entity e, EntityDiffSpec spec) '''
  «FOR s : spec.commonProps.map[cp | cp -> true] + spec.specificProps.map[sp | sp -> false] SEPARATOR '\n'»
    «genPropSpec(e, s.key, s.value)»
  «ENDFOR»
  '''

  def genPropSpec(Entity e, PropertySpec ps, boolean required)
  {
    // http://lambda-the-ultimate.org/node/2694
    if (ps.needsTypeCheck)
      genCodeForTypeCheckProperty(e, ps.property, required)
    else
      genCodeForProperty(ps.property, required);
  }

  // As it is a type check property, it occurs in the 
  def genCodeForTypeCheckProperty(Entity e, Property property, boolean required)
  {
    val typeList = typeListByPropertyName.get(e).get(property.name)
    // On uniqueTypeList we removed duplicated property types, such as a String PrimitiveType and a Reference w originalType String.
    val uniqueTypeList = new ArrayList<Property>();
    // Just a shortcut list so we don't have to access every time to the type field of a property (and all its casts...)
    val typeShortcutList = new ArrayList<String>();

    // This has to be optimized with collections operations..
    for (PropertySpec ps : typeList)
    {
      if (ps.property instanceof Aggregate)
      {
        val typeAggr = ((ps.property as Aggregate).refTo.get(0).eContainer as Entity).name;
        if (!typeShortcutList.exists[type | type.equals(typeAggr)])
        {
          uniqueTypeList.add(ps.property as Aggregate);
          typeShortcutList.add(typeAggr);
        }
      }
      if (ps.property instanceof Reference)
      {
        val typeRef = (ps.property as Reference).originalType;
        if (!typeShortcutList.exists[type | type.equals(typeRef)])
        {
          uniqueTypeList.add(ps.property as Reference);
          typeShortcutList.add(typeRef);
        }
      }
      if (ps.property instanceof Attribute)
      {
        if ((ps.property as Attribute).type instanceof PrimitiveType)
        {
          val typePrimitive = ((ps.property as Attribute).type as PrimitiveType).name;
          if (!typeShortcutList.exists[type | type.equals(typePrimitive)])
          {
            uniqueTypeList.add(ps.property as Attribute);
            typeShortcutList.add(typePrimitive);
          }
        }
        if ((ps.property as Attribute).type instanceof Tuple)
        {
          val typeTuple = ((ps.property as Attribute).type as Tuple).elements;
          if (typeTuple.size == 1)
          {
            uniqueTypeList.add(ps.property as Attribute);
            typeShortcutList.add((typeTuple.get(0) as PrimitiveType).name);
          }
          else if (typeTuple.size > 1 && !typeShortcutList.exists[type | type.equals("Object[]")])
          {
            uniqueTypeList.add(ps.property as Attribute);
            typeShortcutList.add("Object[]");
          }
        }
      }
    }
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

  def String genUnion(Iterable<Property> list, boolean required)
  {
    val theTypes = list.map[p | genTypeForProperty(p)];
    val theName = list.head.name;

  '''
  // @Union_«theTypes.join('_')»
  «IF list.exists[p | p instanceof Aggregate]»@Embedded«ELSE»@Property«ENDIF»
  «IF required»@NotNull(message = "«theName» can't be null")«ENDIF»
  private Object «theName»;
  public Object get«theName.toFirstUpper»() {return this.«theName»;}
  public void set«theName.toFirstUpper»(Object «theName»)
  {
    if («list.map[p | p.name + " instanceof " + genTypeForProperty(p)].join(' || ')»)
      this.«theName» = «theName»;
    else
      throw new ClassCastException("«theName» must be of type «theTypes.join(' or ')»");
  }

  @PreLoad
  private void processUnion_«theTypes.join('_')»(DBObject dbObj)
  {
    if (!dbObj.containsField("«theName»"))
      return;

    Object fieldObj = dbObj.get("«theName»");

    «FOR Property prop : list SEPARATOR '\nelse '»
      «genUnionFor(prop)»
    «ENDFOR»

    dbObj.removeField("«theName»");
  }
  '''
  }

  def dispatch genUnionFor(Aggregate aggr)
  '''
    if (fieldObj instanceof DBObject && ((DBObject)fieldObj).get("className").equals(«(aggr.refTo.head.eContainer as Entity).name».class.getCanonicalName()))
      this.«aggr.name» = Commons.CAST(«(aggr.refTo.head.eContainer as Entity).name».class, fieldObj);
  '''

  def dispatch genUnionFor(Reference ref)
  {
    val typeRef = genTypeForProperty(ref);
    '''
    «IF expandRef(ref).length == 2»
      if (fieldObj instanceof DBObject && ((DBObject)fieldObj).get("$ref").equals("«ref.refTo.name»"))
        this.«ref.name» = ((DBObject)fieldObj).get("$id");
    «ELSE»
      if (fieldObj instanceof «typeRef»)
        this.«ref.name» = («typeRef»)fieldObj;
    «ENDIF»
    '''
  }

  def dispatch genUnionFor(Attribute attr)
  '''
    if (fieldObj instanceof «genAttributeType(attr.type)»)
      this.«attr.name» = («genAttributeType(attr.type)»)fieldObj;
  '''

  def dispatch genCodeForProperty(Aggregate aggr, boolean required)
  '''
    @Embedded
    «IF required»@NotNull(message = "«aggr.name» can't be null")«ENDIF»
    private «genTypeForProperty(aggr)» «aggr.name»;
    public «genTypeForProperty(aggr)» get«aggr.name.toFirstUpper»() {return this.«aggr.name»;}
    public void set«aggr.name.toFirstUpper»(«genTypeForProperty(aggr)» «aggr.name») {this.«aggr.name» = «aggr.name»;}
  '''

  def dispatch genTypeForProperty(Aggregate aggr)
  {
    var entityName = (aggr.refTo.get(0).eContainer as Entity).name;
    if (aggr.upperBound !== 1)
      entityName = entityName + "[]";

    entityName;
  }

  def dispatch genCodeForProperty(Reference ref, boolean required)
  '''
    «IF expandRef(ref).length == 2»@Reference«ELSE»@Property«ENDIF»
    «IF required»@NotNull(message = "«ref.name» can't be null")«ENDIF»
    private «genTypeForProperty(ref)» «ref.name»;
    public «genTypeForProperty(ref)» get«ref.name.toFirstUpper»() {return this.«ref.name»;}
    public void set«ref.name.toFirstUpper»(«genTypeForProperty(ref)» «ref.name») {this.«ref.name» = «ref.name»;}
  '''

  def dispatch genTypeForProperty(Reference ref)
  {
    val refComps = expandRef(ref);
    var returnValue = "";

    // References as DBRef as stored as @Reference private ObjectReferences([])?
    if (refComps.length == 2)
    {
      returnValue = ref.refTo.name;
    }
    // References as Strings or Integers are just stored as @Property private String|Integer([])?, just as usual attributes
    else
    {
      if (ref.originalType === null || ref.originalType.empty)
      {
        returnValue = "String";
      }
      else
      {
        returnValue = ref.originalType;
      }
      returnValue = genAttributeType(returnValue).toString;
    }

    if (ref.upperBound !== 1)
      returnValue = returnValue + "[]";

    returnValue;
  }

  def expandRef(Reference reference) 
  {
    val pat = Pattern.compile("DBRef\\((.+?)\\)")
    val m = pat.matcher(reference.originalType)
    if (m.matches)
      #["dbref", m.group(0)]
    else
      #[reference.originalType]
  }

  def dispatch genCodeForProperty(Attribute a, boolean required)
  '''
    @Property
    «IF required && !a.name.equals("type")»@NotNull(message = "«a.name» can't be null")«ENDIF»
    private «genTypeForProperty(a)» «a.name»;
    public «genTypeForProperty(a)» get«a.name.toFirstUpper»() {return this.«a.name»;}
    public void set«a.name.toFirstUpper»(«genTypeForProperty(a)» «a.name») {this.«a.name» = «a.name»;}
  '''

  def dispatch genTypeForProperty(Attribute a)
  {
    genAttributeType(a.type)
  }

  def dispatch CharSequence genAttributeType(PrimitiveType type)
  {
    genAttributeType(type.name)
  }

  def dispatch CharSequence genAttributeType(Tuple tuple)
  {
    if (tuple.elements.size == 1)
      '''«genAttributeType(tuple.elements.get(0))»[]'''
    else
      // Heterogeneous arrays. Too complex for now...
      '''Object[]'''
  }

  def dispatch CharSequence genAttributeType(String type)
  {
    switch typeName : type.toLowerCase
    {
      case "string" : "String"
      case typeName.isInt : "Integer"
      case typeName.isFloat :  "Float"
      case typeName.isBoolean : "Boolean"
      case typeName.isObjectId : "ObjectId"
      default: ""
    }
  }

  private def isInt(String type) { #["int", "integer", "number"].contains(type)}
  private def isFloat(String type) { #["float", "double"].contains(type)}
  private def isBoolean(String type) { #["boolean", "bool"].contains(type)}
  private def isObjectId(String type) { #["objectid"].contains(type)}

  def specificProps(EntityDiffSpec spec)
  {
    spec.entityVersionProps.map[propertySpecs].fold(<PropertySpec>newHashSet(),
      [result, neew |
        val names = result.map[p | p.property.name].toSet
        result.addAll(neew.filter[p | !names.contains(p.property.name)])
        result
      ])
  }

  // Fill, for each property of each entity that appear in more than 
  // one entity version *with different type* (those that hold the needsTypeCheck
  // boolean attribute), the list of types, to check possible type folding in
  // a latter pass
  def calcTypeListMatrix(List<Entity> entities)
  {
    entities.toInvertedMap[e |
      diffByEntity.get(e).entityVersionProps
        .map[propertySpecs]
        .flatten
        .filter[needsTypeCheck].groupBy[property.name]
    ]
  }

  /**
   * Method used to calculate the dependencies between entities, and reorder them in the correct order
   */
  private def calculateDeps(List<Entity> entities) 
  { 
    entityDeps = newHashMap(entities.map[e | e -> getDepsFor(e)])
    inverseEntityDeps = newHashMap(entities.map[e | 
      e -> entities.filter[e2 | entityDeps.get(e2).contains(e)].toSet
    ])

    // Implement a topological order, Khan's algorithm
    // https://en.wikipedia.org/wiki/Topological_sorting#Kahn.27s_algorithm
    topologicalOrder()
  }

  // Get the first level of dependencies for an Entity
  private def getDepsFor(Entity entity)
  {
    entity.entityversions.map[ev | 
      ev.properties.filter[p | p instanceof Aggregate]
      .map[p | (p as Aggregate).refTo.map[ev2 | ev2.eContainer as Entity]]
      .flatten
    ].flatten.toSet
  }

  private def List<Entity> topologicalOrder()
  {
    depListRec(
      entityDeps.filter[k, v| v.empty].keySet,
      newLinkedList(),
      newHashSet()
    )
  }

  private def List<Entity> depListRec(Set<Entity> to_consider, List<Entity> top_order, Set<Entity> seen)
  {
    // End condition
    if (to_consider.isEmpty)
      top_order
    else
    {
      // Recursive
      val e = to_consider.head
      val to_consider_ = to_consider.tail.toSet

      // Add current node (no dependencies to cover)
      top_order.add(e)
      seen.add(e)

      val dependent = inverseEntityDeps.get(e)
      to_consider_.addAll(
        dependent.filter[ d | seen.containsAll(entityDeps.get(d))
      ])

      depListRec(to_consider_, top_order, seen)
    }
  }

  /**
   * Method used to write a generated CharSequence to a file
   */
  public def static void writeToFile(String filename, CharSequence toWrite)
  {
    val outFile = outputDir.toPath().resolve(filename).toFile()
    val outFileWriter = new PrintStream(outFile)
    outFileWriter.print(toWrite)
    outFileWriter.close()
  }
}

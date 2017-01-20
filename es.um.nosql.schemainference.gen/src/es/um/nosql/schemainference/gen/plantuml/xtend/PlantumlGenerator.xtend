package es.um.nosql.schemainference.gen.plantuml.xtend

import es.um.nosql.schemainference.NoSQLSchema.NoSQLSchema

import es.um.nosql.schemainference.NoSQLSchema.Entity
import es.um.nosql.schemainference.NoSQLSchema.EntityVersion
import es.um.nosql.schemainference.NoSQLSchema.Attribute
import es.um.nosql.schemainference.NoSQLSchema.PrimitiveType
import es.um.nosql.schemainference.NoSQLSchema.Type
import es.um.nosql.schemainference.NoSQLSchema.Tuple
import es.um.nosql.schemainference.NoSQLSchema.Aggregate
import es.um.nosql.schemainference.NoSQLSchema.Reference
import java.util.ArrayList
import java.util.List
class PlantumlGenerator {
  var ArrayList<Entity> entsList=new ArrayList
  EntityVersion evAux
  Entity entAux
  var int indexEl=0
  var int totalEnts=0
  
def generate (NoSQLSchema nosqlschema)
{
  for(lEnt:nosqlschema.entities){
    entAux=lEnt
  	evAux=entAux.entityversions.get(0)
  	if(evAux.root==true){
  	  indexEl++
  	  entsList.add(lEnt)
  	}
  }
  '''
  «FOR entL: entsList»
	«analyzeEnt(entL)»
  «ENDFOR»
  '''
}	
	
def analyzeEnt(Entity ent2)  {
  totalEnts++
  var int totalEntVersions=ent2.entityversions.size
  var int contVer=0;
'''
  «FOR EntityVersion entVer : ent2.entityversions»
    File «ent2.name.toFirstUpper»«contVer+=1»
    @startuml
    title <b> «ent2.name.toFirstUpper»«contVer»
    skinparam backgroundColor AntiqueWhite/Gold 
    skinparam class { 
      BackgroundColor PaleGreen \n
      ArrowColor Blue 
      BorderColor SeaGreen \n
      FontSize 18 \n
      FontName Courier \n
    }
	    			  
    skinparam stereotypeCBackgroundColor YellowGreen
    skinparam stereotypeCBorderColor SpringGreen
    
    Class «ent2.name.toFirstUpper»{
    «var eV=ent2.entityversions.get(contVer-1)»
    «var At=eV.properties.filter(Attribute)»
    «var Ref=eV.properties.filter(Reference)»
    «var Aggreg=eV.properties.filter(Aggregate)»
    «FOR Attribute a: At»
    	«printAttribute(a,a.name)»
    «ENDFOR»
    }
    «FOR Reference r: Ref»
     «printRef(r,ent2.name)»
     «checkRefAggr(r)»
    «ENDFOR»
    «FOR Aggregate ag2: Aggreg»
     «printAgg(ag2,ent2.name)»
     «checkRefAggr(ag2)»
    «ENDFOR»
    @enduml
  «ENDFOR»  
'''
}

def printAgg(Aggregate ag3, String name)'''
  «var Entity entAg=ag3.refTo.get(0).eContainer as Entity»
  «IF ag3.upperBound==-1»
   «name.toFirstUpper» *--> "[1..*] «ag3.name.toLowerCase»" «entAg.name.toFirstUpper»
  «ELSE»
  	«name.toFirstUpper» *--> "[1..1] «ag3.name.toLowerCase»" «entAg.name.toFirstUpper»
  «ENDIF»
'''

def dispatch checkRefAggr(Aggregate aggr){
  if(aggr.refTo!=null)
  {
  	var Entity entAg=aggr.refTo.get(0).eContainer as Entity
  	checkAssociation(aggr.refTo.toList,aggr.name, entAg)
  }
}

def printRef(Reference r, String name)'''

  «IF r.upperBound==1»
   «name» --> "[1..1] «r.name»" «r.refTo.name»
  «ELSE»
   «name» --> "[1..*] «r.name»" «r.refTo.name»
  «ENDIF»
'''

def dispatch checkRefAggr(Reference ref){
  if(ref.refTo!=null)
  {
  	checkAssociation(ref.refTo)
  }
}

//check Reference.refTo
def dispatch checkAssociation(Entity e)'''
  «var ArrayList<Attribute> at = new ArrayList»
  «var ArrayList<Reference> ref = new ArrayList»
  «var ArrayList<Aggregate> aggr =new ArrayList»
  «var ArrayList<String> prims = new ArrayList»
  «var ArrayList<String> tuples = new ArrayList»
  «var ArrayList<Reference> refs=new ArrayList»
  «var ArrayList<Aggregate> ags = new ArrayList»
  «var int contAt=-1»
  «var int contRf=-1»  
  «var int contAgg=-1»      
  «FOR EntityVersion ev:e.entityversions»
     «var a=ev.properties.filter(Attribute)»
     «var r=ev.properties.filter(Reference)»
     «var ag=ev.properties.filter(Aggregate)»
     «FOR i:0..<a.size»
        «at.add(contAt+=1,a.get(i))»
     «ENDFOR»
     «FOR i:0..<r.size»
       «ref.add(contRf+=1,r.get(i))»
     «ENDFOR»
     «FOR i:0..<ag.size»
       «aggr.add(contAgg+=1,ag.get(i))»
     «ENDFOR»
  «ENDFOR»
     
  Class «e.name.toFirstUpper» {
  «FOR Attribute at2: at»
  	«analyzeAttribute(at2.type,at2.name,prims,tuples)»
  «ENDFOR»
  }  
  «FOR Reference rf2: ref»
    «analyzeReference(rf2,rf2.name,refs,e.name)»
  «ENDFOR»
  «FOR Reference rf3: refs»
    «checkRefAggr(rf3)»
  «ENDFOR»
  «FOR Aggregate a3:aggr»
      «analyzeAggregate(a3,a3.name,ags,e.name)»
  «ENDFOR»
  «FOR Aggregate a4: ags»
     «checkRefAggr(a4)»
  «ENDFOR»
'''

//check Aggregate.refTo
def dispatch checkAssociation(List <EntityVersion> agL, String nameAg, Entity entAg)'''
  «var ArrayList<Attribute> at = new ArrayList»
  «var ArrayList<Reference> ref = new ArrayList»
  «var ArrayList<Aggregate> aggr =new ArrayList»
  «var ArrayList<String> prims = new ArrayList»
  «var ArrayList<String> tuples = new ArrayList»
  «var ArrayList<Reference> refs=new ArrayList»
  «var ArrayList<Aggregate> ags = new ArrayList»
  «var int contAt=-1»
  «var int contRf=-1»  
  «var int contAgg=-1»
  «FOR EntityVersion ev:agL»
     «var a=ev.properties.filter(Attribute)»
     «var r=ev.properties.filter(Reference)»
     «var ag=ev.properties.filter(Aggregate)»
     «FOR i:0..<a.size»
        «at.add(contAt+=1,a.get(i))»
     «ENDFOR»
     «FOR i:0..<r.size»
       «ref.add(contRf+=1,r.get(i))»
     «ENDFOR»
     «FOR i:0..<ag.size»
       «aggr.add(contAgg+=1,ag.get(i))»
     «ENDFOR»  «ENDFOR»
  Class «entAg.name.toFirstUpper» {
  «FOR Attribute at2: at»
  	«analyzeAttribute(at2.type,at2.name,prims,tuples)»
  «ENDFOR»
  }  
  «FOR Reference rf2: ref»
    «analyzeReference(rf2,rf2.name,refs,nameAg)»
  «ENDFOR»
  «FOR Reference rf3: refs»
    «checkRefAggr(rf3)»
  «ENDFOR»
  «FOR Aggregate ag: aggr»
      «analyzeAggregate(ag,ag.name,ags,entAg.name)»
  «ENDFOR»
  «FOR Aggregate a4: ags»
    «checkRefAggr(a4)»
  «ENDFOR»
'''

def printAttribute(Attribute a, String name)'''
  «printType(a.type,name)»
'''

def dispatch printType(Type at2, String name) {
  throw new UnsupportedOperationException("TODO: auto-generated method stub")
}

def dispatch printType(PrimitiveType primT, String name){
  if (primT.name=="Number"){
    var t="int"
    '''	<b> «t» «name»'''
  }  
  else
	'''	<b> «primT.name» «name»'''
}

def dispatch printType(Tuple tuple, String name){
  if (tuple.elements.size>0){
    var el=tuple.elements.get(0) as PrimitiveType
    '''	<b> «el.name»[] «name»Tuple'''
  }  
  else
	'''	<b> [] «name»Tuple'''
}

def dispatch analyzeAggregate(Aggregate ag, String name, ArrayList<Aggregate> AgL, String name3) {
	  var boolean rAggreg
	  rAggreg=reviseAggregList(AgL,name)
	  if (!rAggreg)
	    {
	      AgL.add(ag)
	      printAgg(ag,name3)
	    } 
	   
	}
	
	def boolean reviseAggregList(ArrayList<Aggregate> ag, String name) {
	 for (i : 0 ..< ag.size) {
	    val element = ag.get(i)
	    if(element.name==name)
	    	return true
	 }
     return false
	}

//is repeated reference?	
def analyzeReference(Reference ref, String name, ArrayList<Reference> RfL, String name2) {
  var boolean rRef
  rRef=analyzeRefList(RfL,name)
  if (!rRef)
    {
      RfL.add(ref)
      printRef(ref,name2)
      //checkRef(ref)
    }
}
	
def boolean analyzeRefList(ArrayList<Reference> r, String name) {
 for (i : 0 ..< r.size) {
    val element = r.get(i)
    if(element.name==name)
    	return true
 }
    return false
}
	
//for abstract Type class
def dispatch analyzeAttribute(Type at2, String name, ArrayList<String> PrL,ArrayList<String> TuL) {
  throw new UnsupportedOperationException("TODO: auto-generated method stub")
}

def dispatch analyzeAttribute(PrimitiveType primT, String name, ArrayList<String> PrL,ArrayList<String> TuL) {
  var boolean rPrim
  rPrim=analyzePrimList(PrL,primT,name)  
  if (!rPrim){
   	PrL.add(name)
   	if (primT.name=="Number"){
      var t="int"
      '''	<b> «t» «name»'''
    }  
   else
      '''	<b> «primT.name» «name»'''
    }
}
	
def boolean analyzePrimList(ArrayList<String> p, PrimitiveType pr, String name) {
  for (i : 0 ..< p.size) {
    val element = p.get(i)
    if(element==name)
    	return true
  }
  return false
}
	
def dispatch analyzeAttribute(Tuple tuple, String name, ArrayList<String> PrL,ArrayList<String> TuL) {
  var boolean rTuple
  rTuple=analyzeTupleList(TuL,name)  
  if (!rTuple)
   {
   	TuL.add(name)
   	if (tuple.elements.size>0){
      var el=tuple.elements.get(0) as PrimitiveType
      '''	<b> «el.name»[] «name»Tuple'''
    }  
    else
     '''	<b> [] «name»Tuple'''
	}
}
	
def boolean analyzeTupleList(ArrayList<String> t, String name) {
   for (i : 0 ..< t.size) {
    val element = t.get(i)
    if(element==name)
    	return true
    }
    return false
}
	
}
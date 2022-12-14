/*
 * generated by Xtext 2.14.0
 */
package es.um.unosql.xtext.skiql.formatting2

import com.google.inject.Inject

import es.um.unosql.xtext.skiql.metamodel.skiql.SkiQLModel
import es.um.unosql.xtext.skiql.metamodel.skiql.EntityQuery

import org.eclipse.xtext.formatting2.AbstractFormatter2
import org.eclipse.xtext.formatting2.IFormattableDocument

import es.um.unosql.xtext.skiql.services.SkiQLGrammarAccess

class SkiQLFormatter extends AbstractFormatter2 {
	
	@Inject extension SkiQLGrammarAccess
	
	def dispatch void format(SkiQLModel schemaQLModel, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		schemaQLModel.query.format
	}

	def dispatch void format(EntityQuery entityQuery, extension IFormattableDocument document) {
		// TODO: format HiddenRegions around keywords, attributes, cross references, etc. 
		entityQuery.operation.format
		entityQuery.entitySpec.variationFilter.format
	}
	
	// TODO: implement for RelationSpec, ReferenceSpec, EntitySpec, PropertySpec, Property, Before, After, Between
}

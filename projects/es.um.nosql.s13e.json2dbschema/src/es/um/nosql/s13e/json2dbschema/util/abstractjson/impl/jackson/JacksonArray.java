/**
 *
 */
package es.um.nosql.s13e.json2dbschema.util.abstractjson.impl.jackson;

import org.codehaus.jackson.JsonNode;

import es.um.nosql.s13e.json2dbschema.util.abstractjson.IAJArray;

/**
 * @author dsevilla
 *
 */
public class JacksonArray extends JacksonElement implements IAJArray
{
	public JacksonArray(JsonNode val) {
		super(val);
	}
}

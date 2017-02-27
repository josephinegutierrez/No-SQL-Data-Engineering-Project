package es.um.nosql.schemainference.dbgenerator;

import es.um.nosql.schemainference.dbgenerator.utils.DbType;

public class Main
{
	private static final String COUCHDB_IP = "localhost";

	private static final String MONGODB_IP = "localhost";

	private static final String INPUT_FOLDER = "models/";

	private static final String JSON_FOLDER = "json/";

	public static void main(String[] args)
	{
		/**
		 * Minimum and maximum instances generated (at random) for each version
		 * JSON_FOLDER indicates where will be the json files generated
		 * The generated content will be also stored on the database
		 */
		int minInstances = 2;
		int maxInstances = 5;

		DbGenController controller = new DbGenController(DbType.MONGODB, MONGODB_IP);
		controller.startTest(INPUT_FOLDER, JSON_FOLDER, minInstances, maxInstances);
	}
}

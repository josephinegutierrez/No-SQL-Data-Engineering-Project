package es.um.nosql.orchestrator;

import es.um.nosql.orchestrator.util.InferenceMode;
import es.um.nosql.orchestrator.util.constants.ConfigConstants;
import es.um.nosql.s13e.db.util.DbType;

public class Main
{
  public static void main(String[] args)
  {
    Orchestrator inferenceTest = new Orchestrator(DbType.MONGODB);

    //inferenceTest.runCompaniesExample(InferenceMode.FILL_AND_INFER, ConfigConstants.COMPANIES_FILE);                //POJO
    //inferenceTest.runRedditExample(InferenceMode.FILL_ONLY, ConfigConstants.REDDIT_FOLDER);
    //inferenceTest.runEveryPoliticianExample(InferenceMode.INFER_ONLY, ConfigConstants.EVERYPOLITICIAN_FILE);
    //inferenceTest.runFacebookExample(InferenceMode.FILL_AND_INFER, ConfigConstants.FACEBOOK_FOLDER);                //POJO
    //inferenceTest.runHarvardExample(InferenceMode.FILL_AND_INFER, ConfigConstants.HARVARD_FILE);                    //POJO
    //inferenceTest.runJsonExample(InferenceMode.FILL_ONLY, ConfigConstants.JSON_FOLDER);
    //inferenceTest.runLinksExample(InferenceMode.FILL_AND_INFER, ConfigConstants.LINKS_FOLDER);                      //POJO
    ////////inferenceTest.runModelExample(InferenceMode.FILL_AND_INFER, ConfigConstants.MODEL_FILE);
    //inferenceTest.runOpenSanctionsExample(InferenceMode.FILL_ONLY, ConfigConstants.OPENSANCTIONS_FILE);
    //inferenceTest.runPleiadesExample(InferenceMode.FILL_AND_INFER, ConfigConstants.PLEIADES_FILE);                  //TODO: DOESNT WORK YET
    //inferenceTest.runProteinsExample(InferenceMode.FILL_AND_INFER, ConfigConstants.PROTEINS_FOLDER);                //POJO
    //inferenceTest.runPublicationsExample(InferenceMode.FILL_ONLY, ConfigConstants.PUBLICATIONS_FILE);          //POJO
    inferenceTest.runStackOverflowExample(InferenceMode.FILL_ONLY, ConfigConstants.STACKOVERFLOW_FOLDER);
    //inferenceTest.runUrbanDictionaryExample(InferenceMode.INFER_ONLY, ConfigConstants.URBANDICTIONARY_FILE);    //POJO
    //inferenceTest.runWebclickExample(InferenceMode.FILL_AND_INFER, ConfigConstants.WEBCLICKS_FOLDER);              //POJO
    //inferenceTest.runTheMovieDBExample(InferenceMode.FILL_ONLY, ConfigConstants.THEMOVIEDB_FOLDER);

    //TODO: Before checking more datasets, we need to make sure "ObjectMapper oMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);"
    // Is in each interface. Thing is, this is only working por POJO objects and not readTree interfaces.
    // So tldr; datasets loaded without POJO objects are inserting NULL and empty values.
    // Problem with the COMPANY dataset is that it contains A LOT of aggregated objects and null values.
    // Aggregated objects tend to make mongodb run out of memory during the reduce process.
    // Null values tend to abort the inference process. Until the inference process is fixed (TODO(tm)),
    // we will make use of POJO objects and ignore problematic fields. Thing is, then we have a lot of options...
  }
}

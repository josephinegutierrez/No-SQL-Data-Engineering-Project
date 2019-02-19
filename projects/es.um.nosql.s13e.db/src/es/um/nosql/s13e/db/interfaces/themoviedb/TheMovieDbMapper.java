package es.um.nosql.s13e.db.interfaces.themoviedb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TheMovieDbMapper
{
  private static ObjectMapper mapper = new ObjectMapper();

  public static ObjectNode transformTV(ObjectNode tv)
  {
    //TODO: Hmm...for the moment
    tv.remove("created_by");

    // Genre
    ArrayNode arrGenres = mapper.createArrayNode();
    for (JsonNode genre : tv.get("genres"))
      arrGenres.add(genre.get("id").asInt());
    tv.set("genres", arrGenres);

    // Production_companies
    ArrayNode arrCompanies = mapper.createArrayNode();
    for (JsonNode company : tv.get("production_companies"))
      arrCompanies.add(company.get("id").asInt());
    tv.set("production_companies", arrCompanies);

    // Seasons
    ArrayNode arrSeasons = mapper.createArrayNode();
    for (JsonNode season : tv.get("seasons"))
      arrSeasons.add(season.get("id").asInt());
    tv.set("seasons", arrSeasons);

    // Images
    tv.set("images", transformImages((ArrayNode)tv.get("images").get("backdrops"), (ArrayNode)tv.get("images").get("posters")));

    // Credits
    tv.set("credits", transformCredits((ArrayNode)tv.get("credits").get("cast"), (ArrayNode)tv.get("credits").get("crew")));

    tv.set("external_ids", stripNulls((ObjectNode)tv.get("external_ids")));

    // Recommendations
    ArrayNode arrRecommendations = mapper.createArrayNode();
    for (JsonNode recommendation : tv.get("recommendations").get("results"))
      arrRecommendations.add(recommendation.get("id").asInt());
    tv.set("recommended_tv", arrRecommendations);
    tv.remove("recommendations");

    // Similar tv
    ArrayNode arrSimilar = mapper.createArrayNode();
    for (JsonNode similar : tv.get("similar").get("results"))
      arrSimilar.add(similar.get("id").asInt());
    tv.set("similar_tv", arrSimilar);
    tv.remove("similar");

    // Translation
    tv.set("translations", transformTranslations((ObjectNode)tv.get("translations")));

    if (!tv.get("next_episode_to_air").isNull())
      tv.put("next_episode_to_air", tv.get("next_episode_to_air").get("id").asInt());

    if (!tv.get("last_episode_to_air").isNull())
      tv.put("last_episode_to_air", tv.get("last_episode_to_air").get("id").asInt());

    ArrayNode arrNetworks = mapper.createArrayNode();
    for (JsonNode network : tv.get("networks"))
      arrNetworks.add(network.get("id").asInt());
    tv.set("networks", arrNetworks);

    // Keywords
    ArrayNode arrKeywords = mapper.createArrayNode();
    for (JsonNode keyword : tv.get("keywords").get("results"))
      arrKeywords.add(keyword.get("id").asInt());
    tv.set("keywords", arrKeywords);

    // Screened theatrically
    ArrayNode arrScreened = mapper.createArrayNode();
    for (JsonNode screened : tv.get("screened_theatrically").get("results"))
      arrScreened.add(screened.get("id").asInt());
    tv.set("screened_theatrically", arrScreened);

    // Videos
    ArrayNode arrVideos = mapper.createArrayNode();
    for (JsonNode video : tv.get("videos").get("results"))
      arrVideos.add(video.get("id").asText());
    tv.set("videos", arrVideos);

    // Episode_groups
    ArrayNode arrEpGroups = mapper.createArrayNode();
    for (JsonNode epGroup : tv.get("episode_groups").get("results"))
      arrEpGroups.add(epGroup.get("id").asText());
    tv.set("episode_groups", arrEpGroups);

    // Changes
    ArrayNode arrChanges = mapper.createArrayNode();
    for (JsonNode change : tv.get("changes").get("changes"))
      arrChanges.add(change.get("id").asText());
    tv.set("changes", arrChanges);

    // Reviews
    ArrayNode arrReviews = mapper.createArrayNode();
    for (JsonNode review : tv.get("reviews").get("results"))
      arrReviews.add(review.get("id").asText());
    tv.set("reviews", arrReviews);

    // Alternative titles
    ArrayNode arrAltTitles = mapper.createArrayNode();
    arrAltTitles.addAll((ArrayNode)tv.get("alternative_titles").get("results"));
    for (JsonNode altTitle : arrAltTitles)
      stripNulls((ObjectNode)altTitle);
    tv.set("alternative_titles", arrAltTitles);

    // Content ratings
    ArrayNode arrContentRat = mapper.createArrayNode();
    arrContentRat.addAll((ArrayNode)tv.get("content_ratings").get("results"));
    for (JsonNode contentRat : arrContentRat)
      stripNulls((ObjectNode)contentRat);
    tv.set("content_ratings", arrContentRat);

    return stripNulls(renameId(tv, Integer.class));
  }

  public static ArrayNode transformScreenedTheatrically(ArrayNode screened)
  {
    screened.forEach(change ->
    {
      stripNulls(renameId((ObjectNode)change, Integer.class));
    });

    return screened;
  }

  public static ArrayNode transformEpisodeGroups(ArrayNode epGroups)
  {
    epGroups.forEach(epGroup ->
    {
      if (epGroup.has("network") && !epGroup.get("network").isNull())
        ((ObjectNode)epGroup).put("network_id", epGroup.get("network").get("id").asText());
      stripNulls(renameId((ObjectNode)epGroup, String.class));
    });

    return epGroups;
  }

  public static ArrayNode transformChanges(ArrayNode changes)
  {
    changes.forEach(change ->
    {
      stripNulls(renameId((ObjectNode)change, String.class));
    });

    return changes;
  }

  public static ArrayNode transformReviews(ArrayNode reviews)
  {
    reviews.forEach(review ->
    {
      stripNulls(renameId((ObjectNode)review, String.class));
    });

    return reviews;
  }

  public static ArrayNode transformEpisodes(ArrayNode episodes)
  {
    for (JsonNode episode : episodes)
    {
      ObjectNode obj = (ObjectNode)episode;

      if (obj.has("show_id"))
      {
        obj.set("tv_id", obj.get("show_id"));
        obj.remove("show_id");
      }

      obj.set("credits", transformCredits((ArrayNode)obj.get("crew"), null));
      obj.remove("crew");

      stripNulls(renameId(obj, Integer.class));
    }

    return episodes;
  }

  public static JsonNode transformSeason(ObjectNode obj)
  {
    if (obj.has("episodes"))
    {
      ArrayNode arrEpisodes = mapper.createArrayNode();
      for (JsonNode episode : obj.get("episodes"))
        arrEpisodes.add(episode.get("id").asInt());
      obj.set("episodes", arrEpisodes);
    }

    obj.set("images", transformImages((ArrayNode)obj.get("images").get("posters"), null));

    ArrayNode arrVideos = mapper.createArrayNode();
    for (JsonNode video : obj.get("videos").get("results"))
      arrVideos.add(video.get("id").asText());
    obj.set("videos", arrVideos);

    obj.set("credits", transformCredits((ArrayNode)obj.get("credits").get("cast"), (ArrayNode)obj.get("credits").get("crew")));

    return stripNulls(obj);
  }

  public static ArrayNode transformVideo(ObjectNode obj)
  {
    ArrayNode arrVideos = mapper.createArrayNode();

    obj.get("videos").get("results").forEach(video ->
    {
      stripNulls(renameId((ObjectNode)video, String.class));
      arrVideos.add(video);
    });

    return arrVideos;
  }

  public static ObjectNode transformMovie(ObjectNode obj)
  {
    // Genre
    ArrayNode arrGenres = mapper.createArrayNode();
    for (JsonNode genre : obj.get("genres"))
      arrGenres.add(genre.get("id").asInt());
    obj.set("genres", arrGenres);

    // Production_companies
    ArrayNode arrCompanies = mapper.createArrayNode();
    for (JsonNode company : obj.get("production_companies"))
      arrCompanies.add(company.get("id").asInt());
    obj.set("production_companies", arrCompanies);

    for (JsonNode prodCountry : obj.get("production_countries"))
      stripNulls((ObjectNode)prodCountry);

    for (JsonNode spokenLanguage : obj.get("spoken_languages"))
      stripNulls((ObjectNode)spokenLanguage);

    // Images
    obj.set("images", transformImages((ArrayNode)obj.get("images").get("backdrops"), (ArrayNode)obj.get("images").get("posters")));

    // Alternative titles
    ArrayNode arrAltTitles = mapper.createArrayNode();
    arrAltTitles.addAll((ArrayNode)obj.get("alternative_titles").get("titles"));
    for (JsonNode altTitle : arrAltTitles)
      stripNulls((ObjectNode)altTitle);

    obj.set("alternative_titles", arrAltTitles);

    // Videos
    ArrayNode arrVideos = mapper.createArrayNode();
    for (JsonNode video : obj.get("videos").get("results"))
      arrVideos.add(video.get("id").asText());
    obj.set("videos", arrVideos);

    // Credits
    obj.set("credits", transformCredits((ArrayNode)obj.get("credits").get("cast"), (ArrayNode)obj.get("credits").get("crew")));

    // Keywords
    ArrayNode arrKeywords = mapper.createArrayNode();
    for (JsonNode keyword : obj.get("keywords").get("keywords"))
      arrKeywords.add(keyword.get("id").asInt());
    obj.set("keywords", arrKeywords);

    ArrayNode arrDates = mapper.createArrayNode();
    for (JsonNode date : obj.get("release_dates").get("results"))
      for (JsonNode innerDate : date.get("release_dates"))
      {
        ObjectNode objInnerDate = (ObjectNode)innerDate;
        objInnerDate.put("iso_3166_1", date.get("iso_3166_1").asText());
        arrDates.add(objInnerDate);
        stripNulls(objInnerDate);
      }
    obj.set("release_dates", arrDates);

    // Similar movies
    ArrayNode arrSimilar = mapper.createArrayNode();
    for (JsonNode similarMovie : obj.get("similar_movies").get("results"))
      arrSimilar.add(similarMovie.get("id").asInt());
    obj.set("similar_movies", arrSimilar);

    // Recommendations
    ArrayNode arrRecommendations = mapper.createArrayNode();
    for (JsonNode recommendation : obj.get("recommendations").get("results"))
      arrRecommendations.add(recommendation.get("id").asInt());
    obj.set("recommended_movies", arrRecommendations);
    obj.remove("recommendations");

    return stripNulls(renameId(obj, Integer.class));
  }

  public static ObjectNode transformPerson(ObjectNode obj)
  {
    obj.set("external_ids", stripNulls((ObjectNode)obj.get("external_ids")));
    obj.set("translations", transformTranslations((ObjectNode)obj.get("translations")));
    obj.set("credits", transformCredits((ArrayNode)obj.get("combined_credits").get("cast"), (ArrayNode)obj.get("combined_credits").get("crew")));
    obj.remove("combined_credits");
    obj.set("images", transformImages((ArrayNode)obj.get("images").get("profiles"), (ArrayNode)obj.get("tagged_images").get("results")));
    obj.remove("tagged_images");

    return stripNulls(renameId(obj, Integer.class));
  }

  public static ArrayNode transformImages(ArrayNode arr1, ArrayNode arr2)
  {
    ArrayNode arrImages = mapper.createArrayNode();

    if (arr1 != null)
      arrImages.addAll(arr1);
    if (arr2 != null)
      arrImages.addAll(arr2);

    for (JsonNode image : arrImages)
    {
      ObjectNode objImage = (ObjectNode)image;
      if (objImage.has("media_type") && objImage.has("media"))
      {
        objImage.set(objImage.get("media_type").asText() + "_id", objImage.get("media").get("id"));
        objImage.remove("media");
      }

      stripNulls(objImage);
    }

    return arrImages;
  }

  public static ArrayNode transformCredits(ArrayNode arr1, ArrayNode arr2)
  {
    List<String> lAttrCredits = Arrays.asList("person_id", "tv_id", "movie_id", "department", "character", "job", "gender");
    ArrayNode arrCredits = mapper.createArrayNode();

    if (arr1 != null)
      arrCredits.addAll(arr1);
    if (arr2 != null)
      arrCredits.addAll(arr2);

    for (JsonNode credit : arrCredits)
    {
      List<String> removeFields = new ArrayList<String>();
      ObjectNode objCredit = (ObjectNode)credit;

      if (objCredit.has("media_type"))
        objCredit.set(objCredit.get("media_type").asText() + "_id", objCredit.get("id"));
      else
        objCredit.set("person_id", objCredit.get("_id"));

      Iterator<String> fieldNames = objCredit.fieldNames();

      while (fieldNames.hasNext())
      {
        String fName = fieldNames.next();
        if (!lAttrCredits.contains(fName))
          removeFields.add(fName);
      }

      objCredit.remove(removeFields);
      stripNulls(objCredit);
    }

    return arrCredits;
  }

  public static ArrayNode transformTranslations(ObjectNode obj)
  {
    ArrayNode arrTranslations = (ArrayNode)obj.get("translations");
    for (JsonNode translation : arrTranslations)
    {
      ObjectNode objTranslation = (ObjectNode)translation;
      if (translation.get("data").has("biography"))
        objTranslation.set("biography", translation.get("data").get("biography"));
      objTranslation.remove("data");
      // TODO: Beware that translations coming from TV do not have Biography: Instead there are three attributes (name, overview, homepage)
      // For the moment we do ignore these attributes
      stripNulls(objTranslation);
    }

    return arrTranslations;
  }

  public static ObjectNode transformCompany(ObjectNode obj, ArrayNode logos, ArrayNode altNames)
  {
    // Replace parent_company by its id.
    if (obj.has("parent_company") && !obj.get("parent_company").isNull())
    {
      int parentId = obj.get("parent_company").get("id").asInt();
      obj.put("parent_company", parentId);
    }

    // Add an array reference with the logos ids.
    if (logos != null && logos.size() > 0)
    {
      ArrayNode logoRefs = mapper.createArrayNode();
      obj.set("logos", logoRefs);

      logos.forEach(logo -> logoRefs.add(logo.get("_id")));
    }

    // Add alternative_names as embedded objects.
    if (altNames != null && altNames.size() > 0)
      obj.set("alternative_names", altNames);

    return stripNulls(renameId(obj, Integer.class));
  }

  public static ArrayNode transformAlternativeName(ObjectNode obj)
  {
    ArrayNode altNames = mapper.createArrayNode();

    obj.get("results").forEach(altName -> altNames.add(stripNulls((ObjectNode)altName)));

    return altNames;
  }

  public static ArrayNode transformLogo(ObjectNode obj)
  {
    ArrayNode logos = mapper.createArrayNode();

    obj.get("logos").forEach(logo ->
    {
      stripNulls(renameId((ObjectNode)logo, String.class));
      logos.add(logo);
    });

    return logos;
  }

  public static ObjectNode transformNetwork(ObjectNode obj)
  {
    return stripNulls(renameId(obj, Integer.class));
  }

  public static ObjectNode transformGenre(ObjectNode obj)
  {
    return stripNulls(renameId(obj, Integer.class));
  }

  public static ObjectNode transformKeyword(ObjectNode obj)
  {
    return stripNulls(renameId(obj, Integer.class));
  }

  private static ObjectNode stripNulls(ObjectNode obj)
  {
    if (obj == null)
      return obj;

    Iterator<String> fieldNames = obj.fieldNames();
    List<String> nullFields = new ArrayList<String>();

    while (fieldNames.hasNext())
    {
      String fName = fieldNames.next();
      if (obj.get(fName).isNull() || ((obj.get(fName).isArray() || obj.get(fName).isObject()) && obj.get(fName).size() == 0))
        nullFields.add(fName);
    }
    obj.remove(nullFields);

    return obj;
  }

  private static <T> ObjectNode renameId(ObjectNode obj, Class<T> theClass)
  {
    if (obj.has("id"))
    {
      switch (theClass.getName())
      {
        case "java.lang.String": { obj.put("_id", obj.get("id").asText()); break;}
        case "java.lang.Integer": { obj.put("_id", obj.get("id").asInt()); break;}
      }
      obj.remove("id");
    }

    return obj;
  }
}

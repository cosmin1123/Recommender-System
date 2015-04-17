package algorithms.related;

import database.Database;
import database.ItemFamily;
import utils.Item;

import java.util.*;

/**
 * Created by didii on 3/20/15.
 private long dateCreated;
 private String title;
 private String shortTitle;
 private LinkedList<String> keywords;
 private String department;
 private String category;
 private String importance;
 private String publicationId;
 private String language;
 private LinkedList<String> collectionReferences;
 private String author;
 private HashMap<String, Double> ratings;
 */
public class ComputeSimilarity {

    private static boolean ENABLE_CACHING = false;
    public static HashMap<ItemFamily, HashMap<String, Double>> similarityValues;
    private static final double MAX_RATING = 1000000;

    private static double dateCreatedWeight = 12.5;
    private static double titleWeight = 82.0;
    private static double shortTitleWeight = 88.2;
    private static double departmentWeight = 18.7;
    private static double categoryWeight = 18.7;
    private static double importanceWeight = 3; // not tested yet
    private static double languageWeight = 0;
    private static double authorWeight = 33.5;

    private static double keywordWeight = 83.5;
    private static double ratingsWeight = 1.0;
    private static double collectionReferenceWeight = 0.5;
    private static double TFIDFWeight = 12.5;

    private static Double upperBoundKeywords = null;
    private static Double upperBoundCollectionRef = null;

    public static void enableCaching() {
        ENABLE_CACHING = true;

        similarityValues = new HashMap<ItemFamily, HashMap<String, Double>>();

        similarityValues.put(ItemFamily.DATE_CREATED, new HashMap<String, Double>());
        similarityValues.put(ItemFamily.TITLE, new HashMap<String, Double>());
        similarityValues.put(ItemFamily.SHORT_TITLE, new HashMap<String, Double>());
        similarityValues.put(ItemFamily.DEPARTMENT, new HashMap<String, Double>());
        similarityValues.put(ItemFamily.CATEGORY, new HashMap<String, Double>());
        similarityValues.put(ItemFamily.IMPORTANCE, new HashMap<String, Double>());
        similarityValues.put(ItemFamily.PUBLICATION_ID, new HashMap<String, Double>());
        similarityValues.put(ItemFamily.LANGUAGE, new HashMap<String, Double>());
        similarityValues.put(ItemFamily.AUTHOR, new HashMap<String, Double>());
        similarityValues.put(ItemFamily.KEYWORDS, new HashMap<String, Double>());
        similarityValues.put(ItemFamily.RATINGS, new HashMap<String, Double>());
        similarityValues.put(ItemFamily.COLLECTION_REFERENCES, new HashMap<String, Double>());
        similarityValues.put(ItemFamily.TFIDF, new HashMap<String, Double>());
    }

    public static void changeWeights(double newDateCreatedWeight, double newTitleWeight, double newShortTitleWeight,
                                      double newDepartmentWeight, double newCategoryWeight, double newImportanceWeight,
                                      double newLanguageWeight, double newAuthorWeight,
                                      double newKeywordWeight, double newRatingsWeight,
                                      double newCollectionReferenceWeight, double newTFIDFWeight) {
        dateCreatedWeight = newDateCreatedWeight;
        titleWeight = newTitleWeight;
        shortTitleWeight = newShortTitleWeight;
        departmentWeight = newDepartmentWeight;
        categoryWeight = newCategoryWeight;
        importanceWeight = newImportanceWeight;
        languageWeight = newLanguageWeight;
        authorWeight = newAuthorWeight;
        ratingsWeight = newRatingsWeight;
        collectionReferenceWeight = newCollectionReferenceWeight;
        TFIDFWeight = newTFIDFWeight;
        keywordWeight = newKeywordWeight;
    }
    /// TODO LOOOK OVER TFIDF, not ok mate!!!
    private static double getTFIDFweight(HashMap<String, Double> article, HashMap<String, Double> relateArticle,
                                         HashMap<String, Double> idfMap) {
        double sumProdUp = 0;
        double sumDownA = 0;
        double sumDownB = 0;
        double common = 0;


        for(String wordArticle : article.keySet()) {

                Double aValue = article.get(wordArticle);
                Double bValue = relateArticle.get(wordArticle);
                Double wordIdf = idfMap.get(wordArticle);

                if (aValue != null && bValue != null) {

                    aValue *= wordIdf;
                    bValue *= wordIdf;

                    if(aValue != 0 && bValue != 0) {
                        common++;
                    }
                    sumProdUp += (aValue * bValue);
                    sumDownA += (aValue * aValue);
                    sumDownB += (bValue * bValue);
                }
            }

        if(sumDownA == 0 || sumDownB == 0 || common <= 4) {
            return 0;
        }
        return (sumProdUp / (Math.sqrt(sumDownA * sumDownB)));
    }

    private static double getRatingsWeight(HashMap<String, Double> article, HashMap<String, Double> relateArticle) {
        double commonReaders = 0;
        double meanRating = 0;

        Set<String> itemSet = relateArticle.keySet();
        double itemSetSize = itemSet.size();

        for(String key : itemSet) {

            meanRating += (relateArticle.get(key) / itemSetSize);

            if(article.containsKey(key)) {
                commonReaders++;
            }
        }

        return commonReaders * meanRating;
    }

    private static double getListWeight(List<String> list1, List<String> list2, Double upperBound) {
        double similaritySum = 0;

        for(String keyword1 : list1) {
            for(String keyword2 : list2) {
                double similarity = getStringWeight(keyword1, keyword2);
                if(similarity >= 0.5) {
                    similaritySum += similarity;
                }
            }
        }

        if(upperBound == null) {
            return similaritySum;
        }
        return  similaritySum / upperBound;
    }

    private static double getImportanceWeight(String importance) {
        if(importance.equals("high")) {
            return 1;
        } else {
            if(importance.equals("normal")) {
                return 1 / 2;
            } else {
                if(importance.equals("low")) {
                    return 1 / 3;
                }
            }
        }

        return 0;

    }
    private static void letterPairs(String str, ArrayList pairs) {
        int numPairs = str.length() - 1;
        if(numPairs < 0) {
            return ;
        }

        for (int i=0; i < numPairs; i++) {
            pairs.add(str.substring(i,i+2));
        }

    }

    private static ArrayList wordLetterPairs(String str) {
        ArrayList pairs = new ArrayList();

        String[] words = str.split("\\s");

        for (int i=0; i < words.length; i++) {
            letterPairs(words[i], pairs);
        }
        return pairs;
    }

    private static double getStringWeight(String title1, String title2) {
        ArrayList pairs1 = wordLetterPairs(title1.toUpperCase());
        ArrayList pairs2 = wordLetterPairs(title2.toUpperCase());
        double intersection = 0;
        double union = pairs1.size() + pairs2.size();

        for (int i = 0; i < pairs1.size(); i++) {
            Object pair1 = pairs1.get(i);
            for(int j = 0; j < pairs2.size(); j++) {
                Object pair2 = pairs2.get(j);
                if (pair1.equals(pair2)) {
                    intersection++;
                    pairs2.remove(j);
                    break;
                }
            }
        }

        if(union == 0) {
            return 0;
        }

        return ((2.0*intersection) / union);
    }

    private static double getDateWeight(long date1, long date2) {
        if(date1 == 0 || date2 == 0) {
            return 0;
        }

        if(date1 > date2) {
            return (((double) date2) / ((double) date1) - 0.90) * 10;
        } else {
            return (((double) date1) / ((double) date2) - 0.90) * 10;
        }
    }

    private static double getMatchWeight(String value1, String value2) {
        if(value1.equals(value2)) {
            return 1;
        }

        return 0;
    }

    private static double getWeightValue(ItemFamily family, Item relatedArticle, Item article,
                                         HashMap<String, Double> idfMap) {
        double returnValue = 0;
        if(family.equals(ItemFamily.DATE_CREATED)) {
            returnValue = getDateWeight(article.getDateCreated(), relatedArticle.getDateCreated());
        }
        if(family.equals(ItemFamily.TITLE)) {
            returnValue = getStringWeight(article.getTitle(), relatedArticle.getTitle());
        }
        if(family.equals(ItemFamily.SHORT_TITLE)) {
            returnValue = getStringWeight(article.getShortTitle(), relatedArticle.getShortTitle());
        }
        if(family.equals(ItemFamily.CATEGORY)) {
            returnValue = getStringWeight(article.getCategory(), relatedArticle.getCategory());
        }
        if(family.equals(ItemFamily.DEPARTMENT)) {
            returnValue = getStringWeight(article.getDepartment(), relatedArticle.getDepartment());
        }
        if(family.equals(ItemFamily.AUTHOR)) {
            returnValue = getMatchWeight(article.getAuthor(), relatedArticle.getAuthor());
        }
        if(family.equals(ItemFamily.LANGUAGE)) {
            returnValue = getMatchWeight(article.getLanguage(), relatedArticle.getLanguage());
        }
        if(family.equals(ItemFamily.PUBLICATION_ID)) {
            returnValue = getMatchWeight(article.getPublicationId(), relatedArticle.getPublicationId());
        }
        if(family.equals(ItemFamily.IMPORTANCE)) {
            returnValue = getImportanceWeight(relatedArticle.getImportance());
        }
        if(family.equals(ItemFamily.KEYWORDS)) {
            if(upperBoundKeywords == null) {
                upperBoundKeywords = getListWeight(article.getKeywords(),
                        article.getKeywords(), upperBoundKeywords);
            }
            returnValue = getListWeight(article.getKeywords(), relatedArticle.getKeywords(), upperBoundKeywords);
        }
        if(family.equals(ItemFamily.COLLECTION_REFERENCES)) {
            if(upperBoundCollectionRef == null) {
                upperBoundCollectionRef = getListWeight(article.getCollectionReferences(),
                        article.getCollectionReferences(), upperBoundCollectionRef);
            }
            returnValue = getListWeight(article.getCollectionReferences(),
                    relatedArticle.getCollectionReferences(), upperBoundCollectionRef);
        }

        if(family.equals(ItemFamily.RATINGS)) {
            returnValue = getRatingsWeight(article.getRating(), relatedArticle.getRating()) / MAX_RATING;
        }
        if(family.equals(ItemFamily.TFIDF)) {
            if(relatedArticle.getItemId().equals("3002899")) {
                System.out.println("STOP");
            }
            returnValue = getTFIDFweight(article.getTfidf(), relatedArticle.getTfidf(),
                    idfMap);
        }

        return returnValue;
    }

    private static double getSimilarity(ItemFamily family, Item relatedArticle, Item article,
                                        HashMap<String, Double> idfMap) {
        Double returnValue;
        if(ENABLE_CACHING) {
            returnValue = similarityValues.get(family).get(relatedArticle.getItemId());
            if (returnValue == null) {

                returnValue = getWeightValue(family, relatedArticle, article, idfMap);

                similarityValues.get(family).put(relatedArticle.getItemId(), returnValue);
            }
        } else {
            returnValue = getWeightValue(family, relatedArticle, article, idfMap);
        }

        return returnValue;
    }

    public static double getArticleSimilarity(Item article, Item relatedArticle,
                                              HashMap<String, Double> idfMap) {
        // article has always the same words, so get the IDF for them and save it!!!
        Double dateSimilarity = getSimilarity(ItemFamily.DATE_CREATED,relatedArticle, article, idfMap);

        Double titleSimilarity = getSimilarity(ItemFamily.TITLE,relatedArticle, article, idfMap);

        Double shortTitleSimilarity = getSimilarity(ItemFamily.SHORT_TITLE,relatedArticle, article, idfMap);

        Double categorySimilarity = getSimilarity(ItemFamily.CATEGORY,relatedArticle, article, idfMap);

        Double departmentSimilarity = getSimilarity(ItemFamily.DEPARTMENT,relatedArticle, article, idfMap);

        Double authorSimilarity = getSimilarity(ItemFamily.AUTHOR,relatedArticle, article, idfMap);

        Double languageSimilarity = getSimilarity(ItemFamily.LANGUAGE,relatedArticle, article, idfMap);

        Double publicationSimilarity = getSimilarity(ItemFamily.PUBLICATION_ID,relatedArticle, article, idfMap);

        Double importance = getSimilarity(ItemFamily.IMPORTANCE,relatedArticle, article, idfMap);

        Double keywordSimilarity = getSimilarity(ItemFamily.KEYWORDS,relatedArticle, article, idfMap);

        Double collectionSimilarity = getSimilarity(ItemFamily.COLLECTION_REFERENCES,relatedArticle, article, idfMap);

        Double ratingsSimilarity = getSimilarity(ItemFamily.RATINGS,relatedArticle, article, idfMap);

        Double TFIDFSimilarity = getSimilarity(ItemFamily.TFIDF, relatedArticle, article, idfMap);

        return  dateSimilarity * dateCreatedWeight +
                titleSimilarity * titleWeight +
                shortTitleSimilarity * shortTitleWeight +
                authorSimilarity * authorWeight +
                categorySimilarity * categoryWeight +
                departmentSimilarity * departmentWeight +
                languageSimilarity * languageWeight +
                importance * importanceWeight +
                keywordSimilarity * keywordWeight +
                collectionSimilarity * collectionReferenceWeight +
                ratingsSimilarity * ratingsWeight +
                TFIDFSimilarity * TFIDFWeight;
    }
}

package recommendation.algorithm;

import database.Database;
import utils.Item;
import utils.User;

import java.util.*;

/**
 * Created by didii on 3/10/15.
 *
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
public class RelatedArticles {

    private static double dateCreatedWeight = 1;
    private static double titleWeight = 10;
    private static double shortTitleWeight = 5;
    private static double departmentWeight = 7;
    private static double categoryWeight = 10;
    private static double importanceWeight = 9;
    private static double publicationIdWeight = 5;
    private static double languageWeight = 100000;
    private static double authorWeight = 5;

    private static double keywordWeight = 500;
    private static double ratingsWeight = 1;
    private static double collectionReferenceWeight = 5;

    private static double getTFIDFweight(HashMap<String, Double> article, HashMap<String, Double> relateArticle) {
        double sumProdUp = 0;
        double sumDownA = 0;
        double sumDownB = 0;

        HashMap<String, Double> iterateMap = relateArticle;
        HashMap<String, Double> targetMap = article;

        if(article.size() < relateArticle.size()) {
            iterateMap = article;
            targetMap = relateArticle;
        }

        for(String word : iterateMap.keySet()) {
            Double aValue = iterateMap.get(word);
            Double bValue = targetMap.get(word);

            if(aValue != null && bValue != null) {
                sumProdUp += (aValue * bValue);
                sumDownA += (aValue * aValue);
                sumDownB += (bValue * bValue);
            }
        }

        return sumProdUp / (Math.sqrt(sumDownA * sumDownB));
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

        return commonReaders * ratingsWeight * meanRating;
    }

    private static double getListWeight(LinkedList<String> list1, LinkedList<String> list2, double weight) {
        int numKeywords = 0;
        for(String keyword : list1) {
            if(list2.contains(keyword)) {
                numKeywords++;
            }
        }

        return numKeywords * weight;
    }

    private static double getImportanceWeight(String importance) {
        if(importance.equals("high")) {
            return importanceWeight;
        } else {
            if(importance.equals("normal")) {
                return importanceWeight / 2;
            } else {
                if(importance.equals("low")) {
                    return importanceWeight / 3;
                }
            }
        }

        return 0;

    }

    private static double getStringWeight(String title1, String title2, double weight) {
        double commonWordNum = 0;
        double totalDiffWordNum = 0;

        for(String titleWord : title1.split(" ")) {
            if(title2.contains(titleWord)) {
                commonWordNum++;
            }
            totalDiffWordNum++;
        }

        totalDiffWordNum += title2.split(" ").length;
        totalDiffWordNum -= commonWordNum;

        return (commonWordNum / totalDiffWordNum) * weight;
    }

    private static double getDateWeight(long date1, long date2) {
        long diff = Math.abs(date1 - date2);

        if(diff == 0) {
            diff = 1;
        }
        return dateCreatedWeight / diff;
    }

    public static double getArticleSimilarity(Item article, Item relatedArticle) {
        if(relatedArticle.getName().equals("Silken Savior")) {
            System.out.println("STOP IT!!!");
        }
        return getDateWeight(article.getDateCreated(), relatedArticle.getDateCreated()) +
                getStringWeight(article.getTitle(), relatedArticle.getTitle(), titleWeight) +
                getStringWeight(article.getShortTitle(), relatedArticle.getShortTitle(), shortTitleWeight) +
                getStringWeight(article.getAuthor(), relatedArticle.getAuthor(), authorWeight) +
                getStringWeight(article.getCategory(), relatedArticle.getCategory(), categoryWeight) +
                getStringWeight(article.getDepartment(), relatedArticle.getDepartment(), departmentWeight) +
                getStringWeight(article.getLanguage(), relatedArticle.getLanguage(), languageWeight) +
                getStringWeight(article.getPublicationId(), relatedArticle.getPublicationId(), publicationIdWeight) +
                getImportanceWeight(relatedArticle.getImportance()) +
                getListWeight(article.getKeywords(), relatedArticle.getKeywords(), keywordWeight) +
                getListWeight(article.getCollectionReferences(), relatedArticle.getCollectionReferences(),
                        collectionReferenceWeight) +
                getRatingsWeight(article.getRating(), relatedArticle.getRating()) +
                getTFIDFweight(article.getTfidf(), relatedArticle.getTfidf());
    }

    public static LinkedList<String> getCommonReadUsers(String articleId) {
        LinkedList<String> commonReadUsers = new LinkedList<String>();

        Item currentItem = Database.getItem(articleId);

        for(String user : currentItem.getRating().keySet()) {
            commonReadUsers.add(user);
        }

        return commonReadUsers;
    }

    public static LinkedList<Item> recommend(String articleId, int maxArticle) {
        LinkedList<String> commonUsers = getCommonReadUsers(articleId);
        LinkedList<Item> relatedArticles = new LinkedList<Item>();

        Comparator<Double> reverseDoubleComparator = new Comparator<Double>() {
            @Override public int compare( Double s1, Double s2) {
                return (int) (s2 - s1 );
            }
        };
        SortedMap sortedMap = new TreeMap<Double, LinkedList<Item>>(reverseDoubleComparator);



        Item mainItem = Database.getItem(articleId);

        for(String userId : commonUsers) {
            User user = Database.getUser(userId);

            for(String itemId : user.getItemsHistory()) {
                if(itemId.equals(articleId)) {
                    continue;
                }
                Item item = Database.getItem(itemId);
                Double similiarity = getArticleSimilarity(mainItem, item);

                if(!sortedMap.containsKey(similiarity)) {
                    sortedMap.put(similiarity, new LinkedList<Item>());
                }

                ((LinkedList<Item>) sortedMap.get(similiarity)).add(item);
            }
        }
        int relatedArticlesSize = 0;
        for(Object key : sortedMap.keySet()) {

            for(Item item : (LinkedList<Item>) sortedMap.get(key)) {
                relatedArticlesSize++;
                relatedArticles.add(item);
                if(relatedArticlesSize >= maxArticle) {
                    return relatedArticles;
                }

            }
        }

        return relatedArticles;
    }

}

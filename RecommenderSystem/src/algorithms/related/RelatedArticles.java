package algorithms.related;

import algorithms.related.TFIDF.IDF;
import database.Database;
import database.TableName;
import utils.Item;
import utils.User;
import utils.Utils;

import java.util.*;

/**
 * Created by didii on 3/10/15.
 */
public class RelatedArticles {
    private static boolean ENABLE_CACHING = false;
    private static LinkedList<String> cachedCommonUsers = null;
    private static Integer cachedTotalFileNum = null;
    private static Item cachedMainItem = null;
    private static HashMap<String,Double> cachedIdfMap = null;

    public static void enableCaching() {
        ENABLE_CACHING = true;
        ComputeSimilarity.enableCaching();
    }

    public static LinkedList<String> getCommonReadUsers(String articleId) {
        LinkedList<String> commonReadUsers = new LinkedList<String>();

        Item currentItem = Database.getItem(articleId);

        for(String user : currentItem.getRating().keySet()) {
            commonReadUsers.add(user);
        }

        return commonReadUsers;
    }

    public static LinkedList<Item> recommend(String articleId, int maxArticle, boolean useCollaborativeFiltering) {
        LinkedList<String> commonUsers = null;
        Item mainItem;
        Integer totalFileNum;
        HashMap<String,Double> idfMap;

        if(ENABLE_CACHING) {
            if(useCollaborativeFiltering && cachedCommonUsers == null) {
                cachedCommonUsers = getCommonReadUsers(articleId);
            }

            if(cachedMainItem == null) {
                cachedMainItem = Database.getItem(articleId);
            }

            if(cachedTotalFileNum == null) {
                cachedTotalFileNum = Database.getTotalFileNum();
            }

            if(cachedIdfMap == null) {
                cachedIdfMap = new HashMap<String, Double>();
                for(String word : cachedMainItem.getTfidf().keySet()) {
                    if(word.isEmpty()) {
                        continue;
                    }
                    double wordIdf = IDF.getWordIDF(Database.getWordFileAppearance(word), cachedTotalFileNum);
                    cachedIdfMap.put(word, wordIdf);
                }
            }

            commonUsers = cachedCommonUsers;
            mainItem = cachedMainItem;
            idfMap = cachedIdfMap;
        } else {
            if(useCollaborativeFiltering) {
                commonUsers = getCommonReadUsers(articleId);
            }
            mainItem = Database.getItem(articleId);
            totalFileNum = Database.getTotalFileNum();
            idfMap = new HashMap<String, Double>();

            for(String word : mainItem.getTfidf().keySet()) {
                if(word.isEmpty()) {
                    continue;
                }
                double wordIdf = IDF.getWordIDF(Database.getWordFileAppearance(word), totalFileNum);
                idfMap.put(word, wordIdf);
            }
        }

        LinkedList<Item> relatedArticles = new LinkedList<Item>();

        Comparator<Double> reverseDoubleComparator = new Comparator<Double>() {
            @Override public int compare( Double s1, Double s2) {
                return (int) (s2 - s1 );
            }
        };
        SortedMap sortedMap = new TreeMap<Double, LinkedList<Item>>(reverseDoubleComparator);


        if(useCollaborativeFiltering) {
            for (String userId : commonUsers) {
                User user = Database.getUser(userId);

                for (String itemId : user.getItemsHistory()) {
                    if (itemId.equals(articleId)) {
                        continue;
                    }
                    Item item = Database.getItem(itemId);
                    Double similiarity = ComputeSimilarity.getArticleSimilarity(mainItem, item, idfMap);

                    if (!sortedMap.containsKey(similiarity)) {
                        sortedMap.put(similiarity, new LinkedList<Item>());
                    }

                    ((LinkedList<Item>) sortedMap.get(similiarity)).add(item);
                }
            }
        }
        if(sortedMap.size() < maxArticle) {
            List<Item> listItem = Utils.getAllItems(TableName.ITEMS.toString());
            for(Item item : listItem) {
                Double similiarity = ComputeSimilarity.getArticleSimilarity(mainItem, item, idfMap);

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

                // TODO remove ratings
                item.setName(key + "");
                //
                relatedArticles.add(item);
                if(relatedArticlesSize >= maxArticle) {
                    return relatedArticles;
                }

            }
        }

        return relatedArticles;
    }

}

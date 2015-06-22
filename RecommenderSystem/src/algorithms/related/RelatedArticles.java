package algorithms.related;

import algorithms.related.TFIDF.IDF;
import database.Database;
import database.TableName;
import utils.Item;
import utils.SimilarityWeights;
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

    private static LinkedList<String> getCommonReadUsers(String articleId, String publicationId) {
        LinkedList<String> commonReadUsers = new LinkedList<String>();

        Item currentItem = Database.getItem(articleId, publicationId);
        if(currentItem == null) {
            return null;
        }
        for(String user : currentItem.getRating().keySet()) {
            commonReadUsers.add(user);
        }

        return commonReadUsers;
    }

    public static LinkedList<Item> recommend(String articleId, int maxArticle, boolean useCollaborativeFiltering,
                                             String publicationId, SimilarityWeights similarityWeights) {
        LinkedList<String> commonUsers = null;
        Item mainItem;
        Integer totalFileNum;
        HashMap<String,Double> idfMap;

        if(ENABLE_CACHING) {
            if(useCollaborativeFiltering && cachedCommonUsers == null) {
                cachedCommonUsers = getCommonReadUsers(articleId, publicationId);
            }

            if(cachedMainItem == null) {
                cachedMainItem = Database.getItem(articleId, publicationId);
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
                commonUsers = getCommonReadUsers(articleId, publicationId);
            }
            mainItem = Database.getItem(articleId, publicationId);
            if(mainItem == null) {
                return null;
            }
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

        HashMap<Double, LinkedList<Item>> sortedMap = new HashMap<Double, LinkedList<Item>>();

        // use collaborative filtering
        if(useCollaborativeFiltering) {
            for (String userId : commonUsers) {
                User user = Database.getUser(userId);

                for (String itemId : user.getItemsHistory()) {
                    if (itemId.equals(articleId)) {
                        continue;
                    }
                    Item item = Database.getItem(itemId, publicationId);
                    Double similiarity = ComputeSimilarity.getArticleSimilarity(mainItem, item, idfMap,
                            similarityWeights);

                    if (!sortedMap.containsKey(similiarity)) {
                        sortedMap.put(similiarity, new LinkedList<Item>());
                    }
                    item.setScore(similiarity);
                    (sortedMap.get(similiarity)).add(item);
                }
            }
        }

        // compare items to each other
        if(sortedMap.size() < maxArticle) {
            LinkedList<Item> listItem = Utils.getAllItems(TableName.ITEMS.toString(), publicationId);
            if(ENABLE_CACHING) {
                assert listItem != null;
                for(Item item : listItem) {
                    if(item.equals(mainItem)) {
                        continue;
                    }
                    Double similiarity = ComputeSimilarity.getArticleSimilarity(mainItem, item, idfMap,
                            similarityWeights);
                    item.setScore(similiarity);
                    if (!sortedMap.containsKey(similiarity)) {
                        sortedMap.put(similiarity, new LinkedList<Item>());
                    }

                    (sortedMap.get(similiarity)).add(item);
                }
            }
            else{
                listItem.remove(mainItem);
                ComputeSimilarityThreadPool pool = new ComputeSimilarityThreadPool(8, listItem,
                        sortedMap, idfMap, mainItem, similarityWeights);

                pool.startThreads();
                pool.waitToFinish();
                listItem.add(mainItem);
            }
        }


        int relatedArticlesSize = 0;

        Set<Double> keySet = sortedMap.keySet();
        Double[] tmpSet = new Double[keySet.size()];

        keySet.toArray(tmpSet);
        Arrays.sort(tmpSet);


        for(int i = tmpSet.length - 1; i >= 0; i--) {
            Object key = tmpSet[i];

            for(Item item : sortedMap.get(key)) {
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

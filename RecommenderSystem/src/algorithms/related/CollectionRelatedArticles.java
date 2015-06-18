package algorithms.related;

import database.Database;
import utils.Item;
import utils.SimilarityWeights;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by didii on 6/5/15.
 */
public class CollectionRelatedArticles {
    public static LinkedList<Item> related(LinkedList<String>targetIds, String publicationId, int maxArticle,
                                           SimilarityWeights similarityWeights) {
        HashMap<Item, Double> unsortMap = new HashMap<Item, Double>();
        LinkedList<Item> result = new LinkedList<Item>();

        for(String itemId : targetIds) {
            Item item = Database.getItem(itemId, publicationId);

            LinkedList<Item> list = RelatedArticles.recommend(item.getItemId(), 10, false,
                    publicationId, similarityWeights);

            for(Item it : list) {
                Double current = unsortMap.get(it);
                if(current == null) {
                    current = 0.0d;
                }
                current += it.getScore();
                unsortMap.put(it, current);
            }
        }

        Set<Item> keys = unsortMap.keySet();
        TreeMap<Double, LinkedList<Item>> sortedMap = new TreeMap<Double, LinkedList<Item>>();

        for(Item item : keys) {
            Double score = item.getScore();
            LinkedList<Item> itemList = sortedMap.get(score);
            if(itemList == null) {
                itemList = new LinkedList<Item>();
            }
            itemList.add(item);
            sortedMap.put(score, itemList);
        }

        Object[] scoreSet = sortedMap.keySet().toArray();
        for(int i = scoreSet.length - 1; i >= 0; i--) {
            result.addAll(sortedMap.get(scoreSet[i]));

            if(result.size() >= maxArticle) {
                LinkedList<Item> temp = new LinkedList<Item>(result);
                for(Item item : temp) {
                    if(targetIds.contains(item.getItemId())) {
                        result.remove(item);
                    }
                }

                if(result.size() >= maxArticle) {
                    return  result;
                }


            }
        }



        return result;
    }
}

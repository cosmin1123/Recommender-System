package recommendation.algorithm.TFIDF;

import database.Database;
import database.TableName;
import utils.Item;
import utils.Utils;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by didii on 3/13/15.
 */
public class TFIDF {

    private static void computeForItem(Item item, int totalItemNum) {
        HashMap<String, Double> wordMap = TF.getArticleWordFrequency(item.getContent());

        for(String word : wordMap.keySet()) {
            Integer currentFileAppearance = Database.getWordFileAppearance(word);
            currentFileAppearance += 1;
            Database.setWordFileAppearance(word, currentFileAppearance);

            Double wordFrequency = wordMap.get(word);
            Double wordIDF = IDF.getWordIDF(currentFileAppearance, totalItemNum);

            wordMap.put(word, wordFrequency * wordIDF);
        }

        Database.setWordFrequency(item.getItemId(), wordMap);
    }

    public static void computeForAll() {
        LinkedList<Item> itemList = Utils.getAllItems(TableName.ITEMS.toString());
        int totalItemNum = itemList.size();
        for(Item item : itemList) {
            computeForItem(item, totalItemNum);
        }
    }
}

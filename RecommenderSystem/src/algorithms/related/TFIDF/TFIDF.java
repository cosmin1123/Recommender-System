package algorithms.related.TFIDF;

import com.google.common.collect.MinMaxPriorityQueue;
import database.CreateTable;
import database.Database;
import database.TFIDFFamily;
import database.TableName;
import javafx.util.Pair;
import utils.Item;
import utils.Utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by didii on 3/13/15.
 */
public class TFIDF {

    public static void computeForItem(Item item) {
        HashMap<String, Double> wordMap = TF.getArticleWordFrequency(item.getContent());
        HashMap<String, Double> resultMap = new HashMap<String, Double>();

        Comparator<Pair<String, Double>> myComparator = new Comparator<Pair<String, Double>>() {
            @Override
            public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
                return (int)((o2.getValue() - o1.getValue()) * Integer.MAX_VALUE);
            }
        };

        MinMaxPriorityQueue<Pair<String, Double>> queue = MinMaxPriorityQueue.orderedBy(myComparator)
                .maximumSize(100).create();

        int totalFileNum = Database.getTotalFileNum() + 1;
        Database.setTotalFileNum(totalFileNum);

        for(String word : wordMap.keySet()) {
            // words that contain 2 or less letters are prepositions or meaningless words 'an' 'or', etc
            // so we ignore them
            if(word.length() <= 2) {
                continue;
            }
            Integer currentFileAppearance = Database.getWordFileAppearance(word);
            currentFileAppearance += 1;
            Database.setWordFileAppearance(word, currentFileAppearance);

            Double wordFrequency = wordMap.get(word);
            Double wordIDF = IDF.getWordIDF(currentFileAppearance, totalFileNum);
            queue.add(new Pair<String, Double>(word, wordIDF * wordFrequency));

            wordMap.put(word, wordFrequency);
        }

        for(Pair<String, Double> pair : queue) {
            String word = pair.getKey();
            resultMap.put(word,wordMap.get(word));
        }

        Database.setWordFrequency(item.getItemId(), resultMap);
    }

    public static void computeForAll() {
        try {
            CreateTable.deleteTFIDF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CreateTable.createTFIDF();

        LinkedList<Item> itemList = Utils.getAllItems(TableName.ITEMS.toString());

        for(Item item : itemList) {
            computeForItem(item);
        }
    }
}

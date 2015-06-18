package algorithms.related.TFIDF;

import com.google.common.collect.MinMaxPriorityQueue;
import database.Database;
import database.TFIDFFamily;
import database.TableName;
import javafx.util.Pair;
import utils.Item;
import utils.Utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by didii on 3/13/15.
 */
public class TF {
    static final int MAX_TF = 100;

    private static int getTotalArticleWordCount(HashMap<String, Integer> map) {
        int count = 0;
        for(String word : map.keySet()) {
            count = count + map.get(word);
        }

        return count;
    }

    private static HashMap<String, Integer> getWordCount(String content) {


        HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
        List<String> contentWords = StanfordLemmatizer.getlemmatizer().lemmatize(content);

        for(String word : contentWords) {
            // words that contain 2 or less letters are prepositions or meaningless words 'an', 'or', 's, etc
            // so we ignore them
            if(word.length() <= 2) {
                continue;
            }
            Integer wordCount = wordMap.get(word);

            if(wordCount == null) {
                wordCount = 0;
            }

            wordMap.put(word, wordCount + 1);
        }

        return wordMap;
    }

    public static HashMap<String, Double> getArticleWordFrequency(String articleContent) {
        HashMap<String, Integer> wordCountMap = getWordCount(articleContent);
        double totalArticleWordFrequency = (double)getTotalArticleWordCount(wordCountMap);
        HashMap<String, Double> articleWordFrequency = new HashMap<String, Double>();


        for(String word : wordCountMap.keySet()) {
            double wordCount = (double) wordCountMap.get(word);

            articleWordFrequency.put(word, Math.sqrt((wordCount) / (totalArticleWordFrequency)));

        }
        return articleWordFrequency;
    }

    public static void computeTFForItem(Item item, String publicationId) {
        HashMap<String, Double> wordMap = TF.getArticleWordFrequency(item.getContent());
        HashMap<String, Double> resultMap = new HashMap<String, Double>();

        Comparator<Pair<String, Double>> myComparator = new Comparator<Pair<String, Double>>() {
            @Override
            public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
                return (int)((o2.getValue() - o1.getValue()) * Integer.MAX_VALUE);
            }
        };

        MinMaxPriorityQueue<Pair<String, Double>> queue = MinMaxPriorityQueue.orderedBy(myComparator)
                .maximumSize(MAX_TF).create();

        int totalFileNum = Database.getTotalFileNum();


        for(String word : wordMap.keySet()) {

            Integer currentFileAppearance = Database.getWordFileAppearance(word);

            Double wordFrequency = wordMap.get(word);
            Double wordIDF = IDF.getWordIDF(currentFileAppearance, totalFileNum);
            queue.add(new Pair<String, Double>(word, wordIDF * wordFrequency));

            wordMap.put(word, wordFrequency);
        }

        for(Pair<String, Double> pair : queue) {
            String word = pair.getKey();
            resultMap.put(word,wordMap.get(word));
        }
        resultMap.put(TFIDFFamily.TOTAL_FILE_APPEARANCES.toString(), (double) wordMap.size());
        Database.setWordFrequency(item.getItemId(), resultMap, publicationId);
    }

    public static void computeTFForAll(String publicationId) {
        LinkedList<Item> itemList = Utils.getAllItems(TableName.ITEMS.toString(), publicationId);

        assert itemList != null;
        for(Item item : itemList) {
            computeTFForItem(item, publicationId);
        }
    }
}

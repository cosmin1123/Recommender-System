package recommendation.algorithm.TFIDF;

import java.util.HashMap;

/**
 * Created by didii on 3/13/15.
 */
public class TF {
    private static int getTotalArticleWordCount(HashMap<String, Integer> map) {
        int count = 0;
        for(String word : map.keySet()) {
            count = count + map.get(word);
        }

        return count;
    }

    private static HashMap<String, Integer> getWordCount(String content) {
        HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
        String[] contentWords = content.split("[\\.,\\s!;?:'\"]+");

        for(String word : contentWords) {
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
        Integer totalArticleWordFrequency = getTotalArticleWordCount(wordCountMap);
        HashMap<String, Double> articleWordFrequency = new HashMap<String, Double>();


        for(String word : wordCountMap.keySet()) {
            Integer wordCount = wordCountMap.get(word);

            articleWordFrequency.put(word, (Math.log(wordCount + 1) / Math.log(totalArticleWordFrequency)));

        }
        return articleWordFrequency;
    }
}

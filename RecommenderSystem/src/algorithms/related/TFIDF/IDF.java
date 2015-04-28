package algorithms.related.TFIDF;

import database.Database;
import utils.Item;

import java.util.HashSet;
import java.util.List;

/**
 * Created by didii on 3/13/15.
 */
public class IDF {

    public static void addIdfToDatabase(Item item) {
        int totalFileNum = Database.getTotalFileNum() + 1;
        Database.setTotalFileNum(totalFileNum);

        List<String> lst = StanfordLemmatizer.getlemmatizer().lemmatize(item.getContent());
        HashSet<String> checkedWord = new HashSet<String>();

        for(String word : lst) {
            if(checkedWord.contains(word)) {
                continue;
            }
            checkedWord.add(word);
            Integer currentFileAppearance = Database.getWordFileAppearance(word);
            currentFileAppearance += 1;
            Database.setWordFileAppearance(word, currentFileAppearance);
        }
    }

    public static double getWordIDF( double articleWordFrequency, int totalItemNum) {
        if(articleWordFrequency == 0) {
            return 0;
        }
        double idf = 1 + Math.log(totalItemNum / articleWordFrequency);

        return idf;
    }
}

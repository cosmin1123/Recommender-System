package recommendation.algorithm.TFIDF;

import database.Database;

/**
 * Created by didii on 3/13/15.
 */
public class IDF {

    public static double getWordIDF( double articleWordFrequency, int totalItemNum) {
        return totalItemNum / articleWordFrequency;
    }
}

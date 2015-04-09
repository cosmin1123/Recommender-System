package algorithms.related.TFIDF;

/**
 * Created by didii on 3/13/15.
 */
public class IDF {

    public static double getWordIDF( double articleWordFrequency, int totalItemNum) {
        if(articleWordFrequency == 0) {
            return 0;
        }
        double idf = Math.log(totalItemNum / articleWordFrequency);

        return idf;
    }
}

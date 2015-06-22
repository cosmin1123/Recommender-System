package algorithms.related.TFIDF;

import database.TFIDFFamily;

import java.util.HashMap;

/**
 * Created by didii on 3/13/15.
 */
public class TFIDF {
    public static double root(double num, double root) {
        return Math.pow(Math.E, Math.log(num) / root);
    }

    public static Double compute(HashMap<String, Double> article, HashMap<String, Double> relateArticle,
                                 HashMap<String, Double> idfMap) {
        double sumProdUp = 0;
        double sumDownA = 0;
        double sumDownB = 0;
        double common = 0;
        Double relateArticleWordCount = relateArticle.get(TFIDFFamily.TOTAL_FILE_APPEARANCES.toString());
        if (relateArticleWordCount == null) {
            return 0.0;
        }
        for (String wordArticle : article.keySet()) {
            if (wordArticle.equals(TFIDFFamily.TOTAL_FILE_APPEARANCES.toString())) {
                continue;
            }
            Double aValue = article.get(wordArticle);
            Double bValue = relateArticle.get(wordArticle);
            Double wordIdf = idfMap.get(wordArticle);

            if (aValue != null && bValue != null) {

                aValue *= wordIdf;
                bValue *= wordIdf;

                if (aValue != 0 && bValue != 0) {
                    common++;
                }
                sumProdUp += (aValue * bValue);
                sumDownA += (aValue * aValue);
                sumDownB += (bValue * bValue);
            }
        }

        if (sumDownA == 0 || sumDownB == 0) {
            return 0.0;
        }
        // multiply with 30 in order to get aprox 1
        // make root of 5 in order to not influence too much on the article size.
        return (common * 30 / TF.MAX_TF) *
                (sumProdUp / (Math.sqrt(sumDownA * sumDownB))) *
                (1 / root(relateArticleWordCount, 5));
    }
}

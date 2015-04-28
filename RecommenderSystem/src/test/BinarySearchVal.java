package test;

import algorithms.related.RelatedArticles;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by didii on 4/24/15.
 */
public class BinarySearchVal {

    public static void testVal(boolean interactive, String id) {
        RelatedArticles.enableCaching();

        do {
            RelatedArticlesCheck.test(interactive, id);
            CheckFile.test(id);
        } while (interactive);
    }

    public static void test(String id) {
        RelatedArticles.enableCaching();

        Double dateCreatedWeight = 100.0;
        Double titleWeight = 100.0;//3125;
        Double shortTitleWeight = 100.0;//8125;
        Double departmentWeight = 100.0;//5;//1.0;
        Double categoryWeight = 100.0;//5;//3.0;
        Double authorWeight = 100.0;//9375;//3.0;
        Double keywordWeight = 100.0;//6.0;
        Double ratingsWeight = 100.0;//0.0;
        Double TFIDFWeight = 100.0;//6.0;
        Double collectionReferenceWeight = 100.0;//1.0;
        double currentStep = 100;

        while (currentStep > 1) {
            RelatedArticlesCheck.test(false, id, dateCreatedWeight, titleWeight, departmentWeight,
                    shortTitleWeight, categoryWeight, ratingsWeight,
                    authorWeight, keywordWeight, collectionReferenceWeight,
                    TFIDFWeight, currentStep);


            String result = CheckFile.test(id).get(0);

            String[] valueLines = result.split("\n");

            for(String line : valueLines) {
                if(line.split(" ").length <= 1) {
                    continue;
                }
                double val = Double.parseDouble(line.split(" ")[1]);
                if(line.startsWith(RelatedArticlesCheck.DATE_TAG)) {
                    dateCreatedWeight = val;
                }
                if(line.startsWith(RelatedArticlesCheck.TITLE_TAG)) {
                    titleWeight = val;
                }
                if(line.startsWith(RelatedArticlesCheck.SHORTTITLE_TAG)) {
                    shortTitleWeight = val;
                }
                if(line.startsWith(RelatedArticlesCheck.DEPARTMENT_TAG)) {
                    departmentWeight =val;
                }
                if(line.startsWith(RelatedArticlesCheck.CATEGORY_TAG)) {
                    categoryWeight = val;
                }
                if(line.startsWith(RelatedArticlesCheck.AUTHOR_TAG)) {
                    authorWeight = val;
                }
                if(line.startsWith(RelatedArticlesCheck.KEYWORDS_TAG)) {
                    keywordWeight = val;
                }
                if(line.startsWith(RelatedArticlesCheck.RATINGS_TAG)) {
                    ratingsWeight = val;
                }
                if(line.startsWith(RelatedArticlesCheck.TFIDF_TAG)) {
                    TFIDFWeight = val;
                }
                if(line.startsWith(RelatedArticlesCheck.COLLECTIONREF_TAG)) {
                    collectionReferenceWeight = val;
                    // last input in line
                    break;
                }

            }
            currentStep /= 2;
        }
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("out/bestWeights", true)));
            String tempText = id + "\n" +

                    RelatedArticlesCheck.DEPARTMENT_TAG + departmentWeight + "\n" +
                    RelatedArticlesCheck.CATEGORY_TAG + categoryWeight + "\n" +
                    RelatedArticlesCheck.AUTHOR_TAG + authorWeight + "\n" +
                    RelatedArticlesCheck.DATE_TAG + dateCreatedWeight + "\n" +
                    RelatedArticlesCheck.TITLE_TAG + titleWeight + "\n" +
                    RelatedArticlesCheck.SHORTTITLE_TAG + shortTitleWeight + "\n" +
                    RelatedArticlesCheck.KEYWORDS_TAG + keywordWeight + "\n" +
                    RelatedArticlesCheck.RATINGS_TAG + ratingsWeight + "\n" +
                    RelatedArticlesCheck.TFIDF_TAG + TFIDFWeight + "\n" +
                    RelatedArticlesCheck.COLLECTIONREF_TAG + collectionReferenceWeight + "\n";
            out.println(tempText);
            System.out.println(tempText);
            out.close();
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }



    }
}

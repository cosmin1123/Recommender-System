package test;

import algorithms.related.ComputeSimilarity;
import algorithms.related.RelatedArticles;
import database.ItemFamily;
import utils.Item;
import utils.SimilarityWeights;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by didii on 3/24/15.
 */
public class RelatedArticlesCheck {
    private static PrintWriter writer = null;


    public static final String DEPARTMENT_TAG = "Department: ";
    public static final String CATEGORY_TAG = "Category: ";
    public static final String AUTHOR_TAG = "Author: ";
    public static final String DATE_TAG = "Date: ";
    public static final String TITLE_TAG = "Title: ";
    public static final String SHORTTITLE_TAG = "ShortTitle: ";
    public static final String KEYWORDS_TAG = "Keywords: ";
    public static final String RATINGS_TAG = "Ratings: ";
    public static final String TFIDF_TAG = "TFIDF: ";
    public static final String COLLECTIONREF_TAG = "CollectionRef: ";

    public static void printSimilarities(int id, int maxArticle) {
        RelatedArticles.enableCaching();
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("out/simData"), 100000));
        } catch (IOException e) {
            e.printStackTrace();
        }

        LinkedList<Item> itemLinkedList = RelatedArticles.recommend(id + "", maxArticle, false, "",
                new SimilarityWeights());

        for(Item item : itemLinkedList) {
            writer.println(item.getItemId() + "\n" +
                    ItemFamily.DATE_CREATED.toString() + ": " +
                    ComputeSimilarity.similarityValues.get(ItemFamily.DATE_CREATED).get(item.getItemId()) + "\n" +
                            ItemFamily.AUTHOR.toString() + ": " +
                            ComputeSimilarity.similarityValues.get(ItemFamily.AUTHOR).get(item.getItemId()) + "\n" +
                            ItemFamily.CATEGORY.toString() + ": " +
                            ComputeSimilarity.similarityValues.get(ItemFamily.CATEGORY).get(item.getItemId()) + "\n" +
                            ItemFamily.COLLECTION_REFERENCES.toString() + ": " +
                            ComputeSimilarity.similarityValues.get(ItemFamily.COLLECTION_REFERENCES).get(item.getItemId()) + "\n" +
                            ItemFamily.TFIDF.toString() + ": " +
                            ComputeSimilarity.similarityValues.get(ItemFamily.TFIDF).get(item.getItemId()) + "\n" +
                            ItemFamily.DEPARTMENT.toString() + ": " +
                            ComputeSimilarity.similarityValues.get(ItemFamily.DEPARTMENT).get(item.getItemId()) + "\n" +
                            ItemFamily.SHORT_TITLE.toString() + ": " +
                            ComputeSimilarity.similarityValues.get(ItemFamily.SHORT_TITLE).get(item.getItemId()) + "\n" +
                            ItemFamily.TITLE.toString() + ": " +
                            ComputeSimilarity.similarityValues.get(ItemFamily.TITLE).get(item.getItemId()) + "\n" +
                            ItemFamily.KEYWORDS.toString() + ": " +
                            ComputeSimilarity.similarityValues.get(ItemFamily.KEYWORDS).get(item.getItemId()) + "\n" +
                            ItemFamily.RATINGS.toString() + ": " +
                            ComputeSimilarity.similarityValues.get(ItemFamily.RATINGS).get(item.getItemId()) + "\n" +
                            ItemFamily.IMPORTANCE.toString() + ": " +
                            ComputeSimilarity.similarityValues.get(ItemFamily.IMPORTANCE).get(item.getItemId()) + "\n" +
                            ItemFamily.LANGUAGE.toString() + ": " +
                            ComputeSimilarity.similarityValues.get(ItemFamily.LANGUAGE).get(item.getItemId()) + "\n"
            );
        }

        writer.println("");
        writer.println("-------------------------------------------------");
        writer.println("");

    }

    private static void checkForId(String id, int maxArticle) {

        LinkedList<Item> itemLinkedList = RelatedArticles.recommend(id, maxArticle, false, "", new SimilarityWeights());

        for(Item item : itemLinkedList) {
            writer.println(item.getItemId() + " " + item.getScore());
        }

        writer.println("");
        writer.println("-------------------------------------------------");
        writer.println("");

    }

    public static void testValues(double dateCreatedWeight, double titleWeight, double departmentWeight,
                                  double shortTitleWeight, double categoryWeight, double ratingsWeight,
                                  double authorWeight, double keywordWeight, double collectionReferenceWeight,
                                  double TFIDFWeight, String id) {
        SimilarityWeights similarityWeights = new SimilarityWeights();
        similarityWeights.setDateCreatedWeight(dateCreatedWeight + "");
        similarityWeights.setTitleWeight(titleWeight + "");
        similarityWeights.setDepartmentWeight(departmentWeight + "");
        similarityWeights.setShortTitleWeight(shortTitleWeight + "");
        similarityWeights.setCategoryWeight(categoryWeight + "");
        similarityWeights.setRatingsWeight(ratingsWeight + "");
        similarityWeights.setAuthorWeight(authorWeight + "");
        similarityWeights.setKeywordWeight(keywordWeight + "");
        similarityWeights.setCollectionReferenceWeight(collectionReferenceWeight + "");
        similarityWeights.setTFIDFWeight(TFIDFWeight + "");


        writer.println("===========================" +
                " Current values ===========================");
        writer.println(DEPARTMENT_TAG + departmentWeight);
        writer.println(CATEGORY_TAG + categoryWeight);
        writer.println(AUTHOR_TAG + authorWeight);
        writer.println(DATE_TAG + dateCreatedWeight);
        writer.println(TITLE_TAG + titleWeight);
        writer.println(SHORTTITLE_TAG + shortTitleWeight);
        writer.println(KEYWORDS_TAG + keywordWeight);
        writer.println(RATINGS_TAG + ratingsWeight);
        writer.println(TFIDF_TAG + TFIDFWeight);
        writer.println(COLLECTIONREF_TAG + collectionReferenceWeight);

        checkForId(id, 100);
    }

    public static Double readAndWrite(String write, Scanner scanIn) {

        if(scanIn.hasNextLine()) {

            String line = scanIn.nextLine();
            System.out.println(write + " " +  line);
            return Double.parseDouble(line.substring(0, line.indexOf("/")));
        }
        return 0.0;
    }

    public static double keepInBounds(double num) {
        if(num < 0.0) {
            return 0.0;
        }

        if(num > 100) {
            return 100.0;
        }
        return num;
    }

    public static void test(boolean interactive, String id) {

        Double dateCreatedWeight = 73.4375;
        Double titleWeight = 98.4375;//3125;
        Double shortTitleWeight = 89.0625;//8125;
        Double departmentWeight = 4.6875;//5;//1.0;
        Double categoryWeight = 40.625;//5;//3.0;
        Double authorWeight = 29.6875;//9375;//3.0;
        Double keywordWeight = 64.0625;//6.0;
        Double ratingsWeight = 1.5625;//0.0;
        Double TFIDFWeight = 35.9375;//6.0;
        Double collectionReferenceWeight = 0.0;//1.0;
        double currentStep = 0;

        test(interactive, id, dateCreatedWeight, titleWeight, departmentWeight,
                shortTitleWeight, categoryWeight, ratingsWeight,
                authorWeight, keywordWeight, collectionReferenceWeight,
                TFIDFWeight, currentStep);


    }
    public static void test(boolean interactive, String id, double initialDateCreatedWeight, double initialTitleWeight,
                            double initialDepartmentWeight, double initialShortTitleWeight,
                            double initialCategoryWeight, double initialRatingsWeight,
                            double initialAuthorWeight, double initialKeywordWeight,
                            double initialCollectionReferenceWeight, double initialTFIDFWeight, double currentStep) {
        try {
            if(!interactive || writer == null) {
                writer = new PrintWriter(new BufferedWriter(new FileWriter("out/resultedData"), 100000));
            }

            if(interactive) {

                Scanner scanIn = new Scanner(new FileInputStream("res/weights"));
                double dateCreatedWeight = readAndWrite(DATE_TAG, scanIn);
                double titleWeight = readAndWrite(TITLE_TAG, scanIn);
                double shortTitleWeight = readAndWrite(SHORTTITLE_TAG, scanIn);
                double departmentWeight = readAndWrite(DEPARTMENT_TAG, scanIn);
                double categoryWeight = readAndWrite(CATEGORY_TAG, scanIn);
                double authorWeight = readAndWrite(AUTHOR_TAG, scanIn);
                double keywordWeight = readAndWrite(KEYWORDS_TAG, scanIn);
                double ratingsWeight = readAndWrite(RATINGS_TAG, scanIn);
                double TFIDFWeight = readAndWrite(TFIDF_TAG, scanIn);
                double collectionReferenceWeight = readAndWrite(COLLECTIONREF_TAG, scanIn);

                testValues( dateCreatedWeight, titleWeight, departmentWeight,
                        shortTitleWeight, categoryWeight, ratingsWeight,
                        authorWeight, keywordWeight, collectionReferenceWeight,
                        TFIDFWeight, id);

                scanIn.close();
                writer.flush();
            }else {
                for (double departmentWeight = keepInBounds(initialDepartmentWeight - currentStep);
                     departmentWeight <= keepInBounds(initialDepartmentWeight + currentStep);
                     departmentWeight += currentStep) {
                    for (double categoryWeight = keepInBounds(initialCategoryWeight - currentStep) ;
                         categoryWeight <= keepInBounds(initialCategoryWeight + currentStep);
                         categoryWeight += currentStep) {
                        for (double authorWeight = keepInBounds(initialAuthorWeight - currentStep);
                             authorWeight <= keepInBounds(initialAuthorWeight + currentStep);
                             authorWeight += currentStep) {
                            for (double dateCreatedWeight = keepInBounds(initialDateCreatedWeight - currentStep);
                                 dateCreatedWeight <= keepInBounds(initialDateCreatedWeight + currentStep);
                                 dateCreatedWeight += currentStep) {
                                for (double titleWeight = keepInBounds(initialTitleWeight - currentStep);
                                     titleWeight <= keepInBounds(initialTitleWeight+ currentStep );
                                     titleWeight += currentStep) {
                                    for (double shortTitleWeight = keepInBounds(initialShortTitleWeight - currentStep);
                                         shortTitleWeight <= keepInBounds(initialShortTitleWeight + currentStep)
                                            ; shortTitleWeight += currentStep) {
                                       for (double keywordWeight = keepInBounds(initialKeywordWeight - currentStep);
                                            keywordWeight <= keepInBounds(initialKeywordWeight + currentStep);
                                            keywordWeight += currentStep) {
                                            for (double ratingsWeight = keepInBounds(initialRatingsWeight - currentStep);
                                                 ratingsWeight <= keepInBounds(initialRatingsWeight + currentStep);
                                                 ratingsWeight += currentStep) {
                                                for (double TFIDFWeight = keepInBounds(initialTFIDFWeight - currentStep);
                                                     TFIDFWeight <= keepInBounds(initialTFIDFWeight + currentStep);
                                                     TFIDFWeight += currentStep) {
                                                    for (double collectionReferenceWeight =
                                                         keepInBounds(initialCollectionReferenceWeight- currentStep);
                                                         collectionReferenceWeight <= keepInBounds
                                                                 (initialCollectionReferenceWeight + currentStep);
                                                         collectionReferenceWeight += currentStep) {

                                                        testValues( dateCreatedWeight, titleWeight, departmentWeight,
                                                        shortTitleWeight, categoryWeight, ratingsWeight,
                                                        authorWeight, keywordWeight, collectionReferenceWeight,
                                                        TFIDFWeight, id);
                                                        if(currentStep == 0) {
                                                            writer.close();
                                                            return;
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                writer.close();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

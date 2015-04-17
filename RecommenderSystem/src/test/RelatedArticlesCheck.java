package test;

import algorithms.related.ComputeSimilarity;
import algorithms.related.RelatedArticles;
import algorithms.related.TFIDF.TF;
import database.ItemFamily;
import utils.Item;

import java.io.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by didii on 3/24/15.
 */
public class RelatedArticlesCheck {
    private static PrintWriter writer = null;

    public static void printSimilarities(int id, int maxArticle) {
        RelatedArticles.enableCaching();
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("out/simData"), 100000));
        } catch (IOException e) {
            e.printStackTrace();
        }
        LinkedList<Item> itemLinkedList = RelatedArticles.recommend(id + "", maxArticle, false);

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

    private static void checkForId(int id, int maxArticle) {

        LinkedList<Item> itemLinkedList = RelatedArticles.recommend(id + "", maxArticle, false);

        for(Item item : itemLinkedList) {
            writer.println(item.getItemId() + " " + item.getName());
        }

        writer.println("");
        writer.println("-------------------------------------------------");
        writer.println("");

    }

    public static void testValues(double dateCreatedWeight, double titleWeight, double departmentWeight,
                                  double shortTitleWeight, double categoryWeight, double ratingsWeight,
                                  double authorWeight, double keywordWeight, double collectionReferenceWeight,
                                  double TFIDFWeight) {
        ComputeSimilarity.changeWeights(dateCreatedWeight,
                titleWeight, shortTitleWeight, departmentWeight,
                categoryWeight, 0, 0, authorWeight, keywordWeight,
                ratingsWeight, collectionReferenceWeight, TFIDFWeight);

        writer.println("===========================" +
                " Current values ===========================");
        writer.println("Department: " + departmentWeight);
        writer.println("Category: " + categoryWeight);
        writer.println("Author: " + authorWeight);
        writer.println("Date: " + dateCreatedWeight);
        writer.println("Title: " + titleWeight);
        writer.println("ShortTitle: " + shortTitleWeight);
        writer.println("Keywords: " + keywordWeight);
        writer.println("Ratings: " + ratingsWeight);
        writer.println("TFIDF: " + TFIDFWeight);
        writer.println("CollectionRef: " + collectionReferenceWeight);

        checkForId(3000099, 100);
    }

    public static Double readAndWrite(String write, Scanner scanIn) {

        if(scanIn.hasNextLine()) {

            String line = scanIn.nextLine();
            System.out.println(write + " " +  line);
            return Double.parseDouble(line.substring(0, line.indexOf("/")));
        }
        return 0.0;
    }
    public static void test(boolean interactive) {
        try {
            if(!interactive || writer == null) {
                writer = new PrintWriter(new BufferedWriter(new FileWriter("out/resultedData"), 100000));
            }


            final double MAX_FACTOR = 100;

            Double dateCreatedWeight = 12.5;
            Double titleWeight = 82.0;//3125;
            Double shortTitleWeight = 88.2;//8125;
            Double departmentWeight = 18.7;//5;//1.0;
            Double categoryWeight = 18.7;//5;//3.0;
            Double authorWeight = 33.5;//9375;//3.0;
            Double keywordWeight = 83.5;//6.0;
            Double ratingsWeight = 1.0;//0.0;
            Double TFIDFWeight = 12.5;//6.0;
            Double collectionReferenceWeight = 0.5;//1.0;

            if(interactive) {

                Scanner scanIn = new Scanner(new FileInputStream("res/weights"));
                dateCreatedWeight = readAndWrite("Date: ", scanIn);
                titleWeight = readAndWrite("Title: ", scanIn);
                shortTitleWeight = readAndWrite("ShortTitle: ", scanIn);
                departmentWeight = readAndWrite("Department: ", scanIn);
                categoryWeight = readAndWrite("Category: ", scanIn);
                authorWeight = readAndWrite("author: ", scanIn);
                keywordWeight = readAndWrite("Keyword: ", scanIn);
                ratingsWeight = readAndWrite("ratings: ", scanIn);
                TFIDFWeight = readAndWrite("TFIDF: ", scanIn);
                collectionReferenceWeight = readAndWrite("CollectionRef: ", scanIn);

                testValues( dateCreatedWeight, titleWeight, departmentWeight,
                        shortTitleWeight, categoryWeight, ratingsWeight,
                        authorWeight, keywordWeight, collectionReferenceWeight,
                        TFIDFWeight);

                scanIn.close();
                writer.flush();
            }else {
            /*
1950.0
Department: 18.75
Category: 18.75
Author: 33.59375
Date: 12.5
Title: 82.03125
ShortTitle: 88.28125
Keywords: 83.5
Ratings: 1.0
TFIDF: 12.5
CollectionRef: 0.5
             */
                double currentStep = 0.5;
                for (departmentWeight = 18.75 - currentStep; departmentWeight <= (18.75 + currentStep); departmentWeight += currentStep) {
                    for (categoryWeight = 18.75 - currentStep; categoryWeight <= 18.75 + currentStep; categoryWeight += currentStep) {
                        for (authorWeight = 34.375 - currentStep; authorWeight <= 34.375 + currentStep; authorWeight += currentStep) {
                            for (dateCreatedWeight = 12.5 - currentStep; dateCreatedWeight <= 12.5 + currentStep; dateCreatedWeight += currentStep) {
                                for (titleWeight = 82.8125 - currentStep; titleWeight <= 81.25 + currentStep; titleWeight += currentStep) {
                                    for (shortTitleWeight = 89.0625 - currentStep; shortTitleWeight <= 87.5 + currentStep; shortTitleWeight += currentStep) {
                                       for (keywordWeight = 81.0; keywordWeight <= 99; keywordWeight += currentStep) {
                                            for (ratingsWeight = 0.0; ratingsWeight < 10; ratingsWeight += currentStep) {
                                                for (TFIDFWeight = 12.5 - currentStep; TFIDFWeight <= 12.5 + currentStep; TFIDFWeight += currentStep) {
                                                    for (collectionReferenceWeight = 0.0;
                                                         collectionReferenceWeight < 10; collectionReferenceWeight += currentStep) {

                                                        testValues( dateCreatedWeight, titleWeight, departmentWeight,
                                                        shortTitleWeight, categoryWeight, ratingsWeight,
                                                        authorWeight, keywordWeight, collectionReferenceWeight,
                                                        TFIDFWeight);

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

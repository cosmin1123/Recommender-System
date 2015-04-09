package test;

import algorithms.related.ComputeSimilarity;
import algorithms.related.RelatedArticles;
import algorithms.related.TFIDF.TF;
import database.ItemFamily;
import utils.Item;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by didii on 3/24/15.
 */
public class RelatedArticlesCheck {
    private static PrintWriter writer = null;

    public static void printSimilarities(int id, int maxArticle) {
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

    public static void checkForId(int id, int maxArticle) {

        LinkedList<Item> itemLinkedList = RelatedArticles.recommend(id + "", maxArticle, false);

        for(Item item : itemLinkedList) {
            writer.println(item.getItemId() + " " + item.getName());
        }

        writer.println("");
        writer.println("-------------------------------------------------");
        writer.println("");

    }
    public static void test() {
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("out/resultedData"), 100000));


            final double MAX_FACTOR = 3;

            double dateCreatedWeight = 0.1;
            double titleWeight = 0;
            double shortTitleWeight = 1.3;
            double departmentWeight = 21;
            double categoryWeight = 30;
            double authorWeight = 0.3;
            double keywordWeight = 1.2;
            double ratingsWeight = 0.1;
            double TFIDFWeight = 1.4;
            double collectionReferenceWeight = 0;

            //for(departmentWeight = 0; departmentWeight <= MAX_FACTOR * 10; departmentWeight += 1) {
              //  for(categoryWeight = 0; categoryWeight <= MAX_FACTOR * 10; categoryWeight += 1) {
                    //for(authorWeight = 0;authorWeight < MAX_FACTOR / 2; authorWeight += 0.3) {
                       // for(dateCreatedWeight = 0;dateCreatedWeight < MAX_FACTOR / 20; dateCreatedWeight+= 0.1) {
                            for(titleWeight = 0;titleWeight <= MAX_FACTOR / 3; titleWeight += 0.1) {
                                //for(shortTitleWeight = 0.5; shortTitleWeight < MAX_FACTOR; shortTitleWeight+=0.1) {
                                   // for(keywordWeight = 0;keywordWeight < MAX_FACTOR; keywordWeight+=0.1) {
                                    //    for(ratingsWeight = 0; ratingsWeight < MAX_FACTOR / 20; ratingsWeight += 0.1) {
                                            //for(TFIDFWeight = 0;TFIDFWeight < MAX_FACTOR; TFIDFWeight+=0.1) {
                                                for(collectionReferenceWeight = 0;
                                                    collectionReferenceWeight < MAX_FACTOR; collectionReferenceWeight+=0.1) {

                                                    ComputeSimilarity.changeWeights(dateCreatedWeight,
                                                            titleWeight, shortTitleWeight, departmentWeight,
                                                            categoryWeight, 0, 0, 0, authorWeight, keywordWeight,
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
                                            }
                                        //}
                                  //  }
                                //}
                       //     }
                       // }
                //    }
               // }
           // }

            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

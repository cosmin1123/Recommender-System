package Spring.controllers;

import GenerateTables.fastCompany.GenerateFastCompany;
import GenerateTables.imdb.GenerateIMDB;
import algorithms.related.ComputeSimilarity;
import algorithms.related.RelatedArticles;
import algorithms.related.TFIDF.TFIDF;
import database.AllItemsMapper;
import database.Database;
import database.TableName;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import test.CheckFile;
import test.RelatedArticlesCheck;
import utils.Item;
import utils.Utils;

import java.io.IOException;
import java.util.HashMap;

@ComponentScan
@EnableAutoConfiguration
public class Application {

    static void testVal(boolean interactive) {
        RelatedArticles.enableCaching();

        do {
            RelatedArticlesCheck.test(interactive);
            CheckFile.test();
        } while (interactive);

       // Item article = Database.getItem("3000099");
       // Item relateArticle = Database.getItem("3010223");

       // ComputeSimilarity.getArticleSimilarity(article, relateArticle, new HashMap<String, Double>());

    }

    public static void main(String[] args) {

        //GenerateFastCompany.fillDatabase();

        //testVal(false);

        SpringApplication.run(Application.class, args);


       // RelatedArticlesCheck.printSimilarities(3000099, 37446);
    }
}

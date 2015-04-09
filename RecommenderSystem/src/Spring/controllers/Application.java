package Spring.controllers;

import GenerateTables.fastCompany.GenerateFastCompany;
import GenerateTables.imdb.GenerateIMDB;
import algorithms.related.ComputeSimilarity;
import algorithms.related.RelatedArticles;
import algorithms.related.TFIDF.TFIDF;
import database.AllItemsMapper;
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
import utils.Utils;

import java.io.IOException;

@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {

        GenerateFastCompany.fillDatabase();

  //      RelatedArticles.enableCaching();

     //   SpringApplication.run(Application.class, args);
  //      RelatedArticlesCheck.test();

        //TFIDF.computeForAll();

    //    CheckFile.test();
        //RelatedArticlesCheck.printSimilarities(3000099, 35082);

    }
}

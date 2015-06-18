package Spring.controllers;

import GenerateTables.fastCompany.GenerateFastCompany;
import GenerateTables.movieLens.GenerateMovieLens;
import algorithms.recommended.RecommendedArticles;
import algorithms.related.TFIDF.TF;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import test.Recommendation;
import utils.Item;

import java.util.HashMap;


@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        //GenerateFastCompany.fillDatabase();
        SpringApplication.run(Application.class, args);
        //TF.computeTFForAll();
        //GenerateMovieLens.fillDatabase();

        //Recommendation.test("2");



    }
}

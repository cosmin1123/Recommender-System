package Spring.controllers;

import GenerateTables.fastCompany.GenerateFastCompany;
import GenerateTables.movieLens.GenerateMovieLens;
import algorithms.recommended.RecommendedArticles;
import algorithms.related.TFIDF.TF;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import test.Recommendation;
import utils.Item;

import java.util.HashMap;


@ComponentScan
@EnableAutoConfiguration
public class Application {


    public static void compareSim(String s1, String s2) {
        ILexicalDatabase db = new NictWordNet();
        RelatednessCalculator lin = new HirstStOnge(db);

        String []words1 = s1.split(" ");
        String []words2 = s2.split(" ");
        lin.calcRelatednessOfWords("za", "zap");
        long start = System.currentTimeMillis();
        double sum = 0.0;

        for(String word1 : words1) {
            for(String word2 : words2) {
                double sim = 1.0;
                if(!word1.equals(word2)) {
                    sim = lin.calcRelatednessOfWords(word1, word2);
                }


                sum += sim;
            }
        }



        System.out.println(System.currentTimeMillis() - start);
        System.out.println(2 * sum / (words1.length + words2.length));
    }
    public static void main(String[] args) {
        //GenerateFastCompany.fillDatabase();
        SpringApplication.run(Application.class, args);
        //TF.computeTFForAll();
        //GenerateMovieLens.fillDatabase();

        //Recommendation.test("2");



    }
}

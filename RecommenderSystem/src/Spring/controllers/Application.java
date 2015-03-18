package Spring.controllers;

import GenerateTables.web.GenerateFastCompany;
import database.CreateTable;
import database.ItemFamily;
import database.TableName;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import recommendation.algorithm.TFIDF.IDF;
import recommendation.algorithm.TFIDF.TFIDF;
import utils.Utils;

@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        //CreateTable.intialiseTables();
        //GenerateRandom.fillDatabase();
        //SpringApplication.run(Application.class, args);
        GenerateFastCompany.fillDatabase();
        TFIDF.computeForAll();

    }
}

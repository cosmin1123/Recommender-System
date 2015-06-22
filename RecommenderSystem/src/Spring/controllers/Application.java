package Spring.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


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

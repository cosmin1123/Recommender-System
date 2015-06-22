package Spring.controllers;

/**
 * Created by didii on 6/4/15.
 */

import algorithms.related.TFIDF.TF;
import database.CreateTable;
import database.Database;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utils.Item;

@RestController
public class ArticleAddController {
    @RequestMapping(method = RequestMethod.PUT, value = "/addArticle")
    public String greeting(@RequestParam(value = "article", defaultValue = "") String article,
                           HttpEntity<byte[]> requestEntity) {
        String returnString = "\"The string is empty\"";

        if (article.length() != 0) {
            Item newItem = new Item();
            newItem.fillItemFromJson(article);
            if (newItem.getItemId().isEmpty()) {
                returnString = "\"Article needs to have an id\"";
                return returnString;
            }

            Item tmp = Database.getItem(newItem.getItemId(), newItem.getPublicationId());
            if (tmp == null) {
                CreateTable.intialiseTables(newItem.getPublicationId());
                returnString = "\"Article added succesfully\"";
            } else {
                returnString = "\"Article already exists and has been replaced\"";
            }
            newItem.parsePdfByteStream(requestEntity.getBody());
            Database.addItem(newItem);
            TF.computeTFForItem(newItem, newItem.getPublicationId());


        }
        return returnString;
    }
}

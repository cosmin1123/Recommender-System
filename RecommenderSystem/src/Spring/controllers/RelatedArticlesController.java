package Spring.controllers;

import java.util.LinkedList;

import database.Database;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import utils.Item;
import utils.LinkedListWrapper;

@RestController
public class RelatedArticlesController {

    @RequestMapping(method = RequestMethod.GET, value = "/relatedarticles")
    public LinkedListWrapper<Item> greeting(@RequestParam(value = "itemId", defaultValue = "World") String itemId,
                                            @RequestParam(value = "maxArticle", defaultValue = "1000") String maxArticle) {
        return Database.getRelatedArticles(itemId, Integer.parseInt(maxArticle));
    }
}

package Spring.controllers;

import database.Database;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import utils.Item;
import utils.LinkedListWrapper;
import utils.User;
import utils.Utils;

import java.util.LinkedList;

@RestController
public class FriendRecommendedArticlesController {

    @RequestMapping(method = RequestMethod.GET, value = "/friendrecommendedarticles")
    public LinkedListWrapper<Item> greeting(@RequestParam(value = "userId", defaultValue = "World")
                                                String userId,
                                            @RequestParam(value = "maxArticles", defaultValue = "1000")
                                                String maxArticles) {

        return  Database.getFriendDirectlyRecommendedArticles(userId, Integer.parseInt(maxArticles));
    }
}

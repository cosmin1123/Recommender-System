package Spring.controllers;

        import database.Database;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestMethod;
        import org.springframework.web.bind.annotation.RequestParam;
        import org.springframework.web.bind.annotation.RestController;

        import utils.Item;
        import utils.LinkedListWrapper;

@RestController
public class RecommendedArticlesController {

    @RequestMapping(method = RequestMethod.GET, value = "/recommendedarticles")
    public LinkedListWrapper<Item> greeting(@RequestParam(value = "userId", defaultValue = "World") String userId,
                                            @RequestParam(value = "maxArticle", defaultValue = "1000") String maxArticle) {
        return Database.getRecommendedArticles(userId, Integer.parseInt(maxArticle));
    }
}

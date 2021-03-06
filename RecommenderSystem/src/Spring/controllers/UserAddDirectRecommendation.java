package Spring.controllers;

import database.Database;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAddDirectRecommendation {

    @RequestMapping(method = RequestMethod.PUT, value = "/adddirectrecommendation")
    public void greeting(@RequestParam(value = "userId", defaultValue = "") String userId,
                         @RequestParam(value = "itemId", defaultValue = "") String itemId) {
        if (!userId.isEmpty() && !itemId.isEmpty()) {
            Database.addDirectRecommendation(userId, itemId);
        }
    }
}

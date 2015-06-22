package Spring.controllers;

import database.Database;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utils.User;

@RestController
public class UserAddController {

    @RequestMapping(method = RequestMethod.POST, value = "/useradd")
    public void greeting(@RequestParam(value = "userId", defaultValue = "") String userId) {
        // TODO ASK for the other parameters
        User user = new User();
        user.setUserId(userId);

        if (!userId.isEmpty()) {
            Database.addUser(user);
        }
    }
}

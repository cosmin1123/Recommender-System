package Spring.controllers;

import database.Database;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserDeleteController {

    @RequestMapping(method = RequestMethod.DELETE, value = "/userdelete")
    public void greeting(@RequestParam(value = "userId", defaultValue = "") String userId) {
        if(!userId.isEmpty()) {
            Database.deleteUser(userId);
        }
    }
}

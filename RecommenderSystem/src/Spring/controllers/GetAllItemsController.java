package Spring.controllers;

import database.Database;
import database.TableName;
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
public class GetAllItemsController {

    @RequestMapping(method = RequestMethod.GET, value = "/getallitems")
    public LinkedListWrapper<Item> greeting(@RequestParam(value = "maxArticle", defaultValue = "10")
                                                String maxArticle) {
        LinkedList<Item> list = Utils.getAllItems(TableName.ITEMS.toString());
        LinkedList<Item> result = new LinkedList<Item>();
        int max = Integer.parseInt(maxArticle);

        for(Item item : list) {
            result.add(item);
            max--;
            if(max == 0){
                break;
            }

        }

        return new LinkedListWrapper<Item>(result);
    }
}

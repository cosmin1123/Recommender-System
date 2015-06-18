package Spring.controllers;

import database.TableName;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import utils.Item;
import utils.LinkedListWrapper;
import utils.Utils;

import java.util.LinkedList;

@RestController
public class GetAllItemsController {

    @RequestMapping(method = RequestMethod.GET, value = "/getallitems")
    public LinkedListWrapper<Item> greeting(@RequestParam(value = "maxArticle", defaultValue = "10")
                                                String maxArticle,
                                            @RequestParam(value = "publicationId", defaultValue = "")
                                            String publicationId) {
        LinkedList<Item> list = Utils.getAllItems(TableName.ITEMS.toString(), publicationId);
        LinkedList<Item> result = new LinkedList<Item>();
        int max = Integer.parseInt(maxArticle);

        assert list != null;
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

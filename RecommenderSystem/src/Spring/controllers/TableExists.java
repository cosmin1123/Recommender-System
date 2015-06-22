package Spring.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utils.Utils;

@RestController
public class TableExists {
    @RequestMapping(method = RequestMethod.GET, value = "/tableexists")
    public boolean greeting(@RequestParam(value = "tableName", defaultValue = "") String tableName) {
        // TODO ASK for the other parameters
        if (tableName.length() == 0) {
            return false;
        } else {
            return Utils.tableExists(tableName);
        }
    }
}

package Spring.controllers;

import database.Database;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
public class TestingController {

    @RequestMapping(method = RequestMethod.GET, value = "/testing")
    public String testing(@RequestParam(value="itemId", required=true) String name, Model model)  {
        try {
            File file = new File(("fastCompany/testing.html"));
            FileInputStream htmlData = new FileInputStream(
                    file);
            byte[] data = new byte[(int) file.length()];
            htmlData.read(data);
            htmlData.close();
            return new String(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "greeting";
    }
}

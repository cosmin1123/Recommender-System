package GenerateTables.web;

import database.CreateTable;
import database.Database;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import utils.Item;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by didii on 3/17/15.
 * itemId = "";
 title = "";
 shortTitle = "";
 keywords;
 department;
 category;
 author = "";
 this.content = "";
 dateCreated;
 */
public class GenerateFastCompany {

    public static String makeGetRequest(Integer id) {
        String url = "http://www.fastcompany.com/api/v1/posts/" + id;

        URL obj = null;
        try {
            obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(id + " does not exist");
        }
        return  null;

    }

    public static Item createItem(int articleId) {
        Item newItem = new Item();
        newItem.setItemId(articleId + "");
        String jsonString = makeGetRequest(articleId);
        if(jsonString == null) {
            return null;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);

            newItem.setContent(jsonObject.getString("body"));

            newItem.setTitle(jsonObject.getString("title"));

            newItem.setShortTitle(jsonObject.getString("title"));



            newItem.setKeywords(jsonObject.getJSONObject("keywords").getJSONArray("tags").join(";"));
            newItem.setKeywords(jsonObject.getJSONObject("keywords").getJSONArray("socialtags").join(";"));

            newItem.setAuthor(jsonObject.getJSONObject("author").getString("display_name"));

        } catch (JSONException e) {
            e.printStackTrace();
            newItem = null;
        }

        return newItem;
    }

    public static void fillDatabase() {
        CreateTable.deleteAll();
        CreateTable.intialiseTables();

        for(int i = 3000000; i < 3043600; i++) {
            Item newItem = createItem(i);
            if(newItem != null) {
                Database.addItem(newItem);
                System.out.println("Added " + i);
            }
        }

    }
}

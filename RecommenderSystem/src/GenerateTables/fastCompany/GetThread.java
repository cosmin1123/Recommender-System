package GenerateTables.fastCompany;

import database.Database;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utils.Item;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * Created by didii on 4/7/15.
 */
public class GetThread extends Thread {
    private final int start;
    private final int end;


    public GetThread(int start, int end) {
        this.start = start;
        this.end = end;
    }

    private String makeGetRequest(Integer id) {
        String url = "http://www.fastcompany.com/api/v1/posts/" + id;

        URL obj = null;
        try {
            obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            long startTime = System.currentTimeMillis();

            while (!in.ready()) {

                if ((System.currentTimeMillis() - startTime) > 100) {
                    System.out.println(id + " is blocking BufferedReader");
                    return null;
                }
            }
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(id + " does not exist");
        }
        return null;

    }

    private Item createItem(int articleId) {
        Item newItem = new Item();

        newItem.setItemId(articleId + "");
        String jsonString = makeGetRequest(articleId);
        if (jsonString == null) {
            return null;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);

            // set content
            Document doc = Jsoup.parse(jsonObject.getString("body"));

            newItem.setContent(doc.body().text());

            // set title
            newItem.setTitle(jsonObject.getJSONObject("teaser").getString("plain"));

            // set short title
            newItem.setShortTitle(jsonObject.getString("title"));

            // set keywords
            newItem.setKeywords(jsonObject.getJSONObject("keywords").getJSONArray("tags").join(";"));
            newItem.setKeywords(jsonObject.getJSONObject("keywords").getJSONArray("socialtags").join(";"));

            // set date
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

            long time = f.parse(jsonObject.getString("published").substring(0, 10)).getTime();

            newItem.setDateCreated(time);

            // set author
            newItem.setAuthor(jsonObject.getJSONObject("author").getString("display_name"));

            // set department
            newItem.setDepartment(jsonObject.getJSONObject("analytics").getJSONObject("omniture").getString("channel"));
            if (newItem.getDepartment().isEmpty()) {
                newItem.setDepartment(newItem.getKeywords().
                        get((int) ((newItem.getKeywords().size() - 1) * Math.random())));
            }

            // set category
            newItem.setCategory(jsonObject.getJSONObject("analytics").getJSONObject("omniture").getString("channel"));
            if (newItem.getCategory().isEmpty()) {
                newItem.setCategory(newItem.getKeywords().
                        get((int) ((newItem.getKeywords().size() - 1) * Math.random())));
            }

            LinkedList<String> collectionRef = new LinkedList<String>();

            for (String str : newItem.getKeywords()) {
                if (collectionRef.size() > (Math.random() * 10)) {
                    break;
                }
                collectionRef.add(str);
            }

            newItem.setCollectionReferences(collectionRef);

            HashMap<String, Double> ratings = new HashMap<String, Double>();
            JSONObject statsObj = jsonObject.getJSONObject("stats");
            JSONArray jsonArray = statsObj.names();
            int size = jsonArray.length();

            for (int i = 0; i < size; i++) {
                String name = (String) jsonArray.get(i);
                ratings.put(name, Double.parseDouble(statsObj.getJSONObject(name).getString("count")));
            }

            newItem.setRatings(ratings);

        } catch (JSONException e) {
            e.printStackTrace();
            newItem = null;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newItem;
    }

    @Override
    public void run() {
        System.out.println("Started " + start + " " + end);
        for (int i = start; i < end; i++) {
            Item newItem = createItem(i);
            if (newItem != null) {

                Database.addItem(newItem);
                //TFIDF.computeForItem(newItem);

                System.out.println("Added " + i);
            }
        }
    }
}

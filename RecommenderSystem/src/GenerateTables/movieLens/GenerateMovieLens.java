package GenerateTables.movieLens;

import database.CreateTable;
import database.Database;
import utils.Item;
import utils.User;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by didii on 5/29/15.
 */
public class GenerateMovieLens {
    public static void fillDatabase() {
        CreateTable.deleteAll();
        CreateTable.intialiseTables();

        try {
            BufferedReader buff = new BufferedReader(new FileReader(("res/movieLens.res/list")));

            String line;

            while ((line = buff.readLine()) != null) {
                String[] arr = line.split("\\t");

                String userId = arr[0];
                String itemId = arr[1];
                String rating = arr[2];

                Item tempItem = Database.getItem(itemId, "");
                User tempUser = Database.getUser(userId);

                tempItem.setItemId(itemId);
                tempItem.getRating().put(userId, (double) Integer.parseInt(rating));
                tempUser.setUserId(userId);
                tempUser.getItemsHistory().add(itemId);


                Database.addItem(tempItem);
                Database.addUser(tempUser);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

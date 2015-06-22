package test;

import algorithms.recommended.RecommendedArticles;
import database.Database;
import utils.Item;
import utils.User;

import java.util.LinkedList;

/**
 * Created by didii on 5/29/15.
 */
public class Recommendation {
    static Double variationSum = 0.0d;
    static Double count = 0.0d;

    public static String result(String userId, int index, User user) {

        String itemId = user.getItemsHistory().get(index);
        user.getItemsHistory().remove(index);

        Item item = Database.getItem(itemId, "");
        Double userRating = item.getRating().get(userId);
        item.getRating().remove(userId);

        Database.addItem(item);
        Database.addUser(user);

        LinkedList<Item> recommendedItems = RecommendedArticles.recommend(userId, 100000, "", true);

        for (Item it : recommendedItems) {
            if (it.getItemId().equals(itemId)) {
                variationSum += Math.abs(userRating - it.getScore());
                count++;
                Database.addItem(item);
                Database.addUser(user);
                return (userRating + " " + it.getName());
            }
        }

        user.getItemsHistory().add(itemId);
        item.getRating().put(userId, userRating);
        Database.addItem(item);
        Database.addUser(user);

        return "NO";
    }

    public static void test(String userId) {

        User user = Database.getUser(userId);
        int size = user.getItemsHistory().size();
        System.out.println(size);
        for (int i = 0; i < size; i++) {

            System.out.println(result(userId, i, user));
            System.out.println(variationSum + " " + count + " " + variationSum / count);
        }

        System.out.println(variationSum / count);
    }
}

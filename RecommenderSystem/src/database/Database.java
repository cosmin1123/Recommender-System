package database;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import recommendation.algorithm.RecommendedArticles;
import recommendation.algorithm.RelatedArticles;
import recommendation.algorithm.TFIDF.TFIDF;
import utils.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by didii on 2/24/15.
 */
public class Database {

    public static int getWordFileAppearance(String word) {
        Result rs = Utils.getOneRecord(TableName.TFIDF.toString(), word);
        byte[] byteArray = rs.getValue(Bytes.toBytes(TFIDFFamily.TOTAL_FILE_APPEARANCES.toString()),
                Bytes.toBytes(TFIDFFamily.TOTAL_FILE_APPEARANCES.toString()));

        if(byteArray == null){
            return 1;
        }
        return Integer.parseInt(new String(byteArray));

    }

    public static void setWordFileAppearance(String word, int wordFileAppearance) {
        Utils.addRecord(TableName.TFIDF.toString(), word, TFIDFFamily.TOTAL_FILE_APPEARANCES.toString(),
                TFIDFFamily.TOTAL_FILE_APPEARANCES.toString(), wordFileAppearance + "");
    }

    public static void setWordFrequency(String itemId, HashMap<String, Double> map) {
        for(String word : map.keySet()) {

                Utils.addRecord(TableName.ITEMS.toString(), itemId, ItemFamily.TFIDF.toString(),
                        word, map.get(word) + "");

        }
    }

    public static Item getItem(String itemID) {
        Item currentItem = new Item();

        Result rs = Utils.getOneRecord(TableName.ITEMS.toString(), itemID);
        currentItem.setItemId(itemID);
        for(KeyValue kv : rs.raw()){
            currentItem.addToItem(Enum.valueOf(ItemFamily.class, new String(kv.getFamily())),
                     new String(kv.getValue()), new String(kv.getQualifier()));
        }

        return currentItem;
    }

    public static void addItem(Item item) {
        String book = item.getItemId();
        Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.TITLE.toString(),
                ItemFamily.TITLE.toString(), item.getTitle());
        Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.SHORT_TITLE.toString(),
                ItemFamily.SHORT_TITLE.toString(), item.getShortTitle());
        Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.AUTHOR.toString(),
                ItemFamily.AUTHOR.toString(), item.getAuthor());
        Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.DEPARTMENT.toString(),
                ItemFamily.DEPARTMENT.toString(), item.getDepartment());
        Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.CATEGORY.toString(),
                ItemFamily.CATEGORY.toString(), item.getCategory());
        Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.KEYWORDS.toString(),
                ItemFamily.KEYWORDS.toString(), item.getKeywords().toArray());
        Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.CONTENT.toString(),
                ItemFamily.CONTENT.toString(), item.getContent());
    }

    public static User getUser(String userId) {
        User currentUser = new User();
        currentUser.setUserId(userId);
        Result rs = Utils.getOneRecord(TableName.USERS.toString(), userId);
        for(KeyValue kv : rs.raw()){
            currentUser.addToUser(new String(kv.getFamily()), new String(kv.getValue()));
        }

        return currentUser;
    }

    public static LinkedList<Item> getUserItems(String userId) {
        User user = getUser(userId);
        LinkedList<Item> itemList = new LinkedList<Item>();

        for(String itemId : user.getItemsHistory()) {
            itemList.add(getItem(itemId));
        }

        return  itemList;
    }

    public static LinkedList<Item> getUserRatedItems(String userId) {
        User user = getUser(userId);
        LinkedList<Item> itemList = new LinkedList<Item>();

        for(String itemId : user.getItemsHistory()) {
            Item item = getItem(itemId);
            if (item.getRating().containsKey(userId)) {
                itemList.add(item);
            }
        }

        return  itemList;
    }

    public static LinkedListWrapper<Item> getRelatedArticles (String articleId, int maxArticle) {


        return new LinkedListWrapper<Item>(RelatedArticles.recommend(articleId, maxArticle));

    }

    public static LinkedListWrapper<Item> getRecommendedArticles(String userId, int maxArticle) {
        return new LinkedListWrapper<Item>(RecommendedArticles.recommend(userId, maxArticle));
    }

    public static LinkedListWrapper<Item> getFriendDirectlyRecommendedArticles(String userId, int maxArticles) {
        LinkedList<String> recommendedItem =  Database.getUser(userId).getTopFriends();
        LinkedList<Item> recommendedItemList = new LinkedList<Item>();

        for(String friend : recommendedItem) {
            LinkedList<String> itemList = Database.getUser(friend).getItemsDirectlyRecommended();

            for(String item : itemList) {
                recommendedItemList.add(Database.getItem(item));
                if(recommendedItemList.size() >= maxArticles) {
                    return new LinkedListWrapper<Item>(recommendedItemList);
                }
            }
        }

        return new LinkedListWrapper<Item>(recommendedItemList);
    }

    public static boolean addUser(String userId) {
        System.out.println("USER: " + userId + " added");
        return true;
    }

    public static boolean deleteUser(String userId) {
        System.out.println("USER: " + userId + " deleted");
        return true;
    }

    public static boolean updateUserHistory(String userId, String itemId) {
        System.out.println("USER: " + userId + " updated");
        return true;
    }

    public static boolean addDirectRecommendation(String userId, String itemId) {
        System.out.println("USER: " + userId + " new recommendation");
        return true;
    }



}

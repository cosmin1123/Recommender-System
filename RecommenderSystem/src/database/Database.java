package database;

import algorithms.related.TFIDF.IDF;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import algorithms.recommended.RecommendedArticles;
import algorithms.related.RelatedArticles;
import utils.*;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by didii on 2/24/15.
 */
public class Database {

    public static int getWordFileAppearance(String word) {
        Result rs = Utils.getOneRecord(TableName.TFIDF.toString(), word);
        assert rs != null;
        byte[] byteArray = rs.getValue(Bytes.toBytes(TFIDFFamily.TOTAL_FILE_APPEARANCES.toString()),
                Bytes.toBytes(TFIDFFamily.TOTAL_FILE_APPEARANCES.toString()));

        if(byteArray == null){
            return 0;
        }
        return Integer.parseInt(new String(byteArray));

    }

    public static void setWordFileAppearance(String word, int wordFileAppearance) {
        Utils.addRecord(TableName.TFIDF.toString(), word, TFIDFFamily.TOTAL_FILE_APPEARANCES.toString(),
                TFIDFFamily.TOTAL_FILE_APPEARANCES.toString(), wordFileAppearance + "");
    }

    public static int getTotalFileNum() {
        Result rs = Utils.getOneRecord(TableName.TFIDF.toString(), TFIDFFamily.TOTAL_FILE_NUM.toString());

        assert rs != null;
        byte[] byteArray = rs.getValue(Bytes.toBytes(TFIDFFamily.TOTAL_FILE_NUM.toString()),
                Bytes.toBytes(TFIDFFamily.TOTAL_FILE_NUM.toString()));

        if(byteArray == null) {
            return 0;
        }

        return Integer.parseInt(new String(byteArray));

    }

    public static void setTotalFileNum(int totalFileNum) {
        Utils.addRecord(TableName.TFIDF.toString(), TFIDFFamily.TOTAL_FILE_NUM.toString(),
                TFIDFFamily.TOTAL_FILE_NUM.toString(),
                TFIDFFamily.TOTAL_FILE_NUM.toString(),
                totalFileNum + "");
    }

    public static void setWordFrequency(String itemId, HashMap<String, Double> map, String publicationId) {
        for(String word : map.keySet()) {

                Utils.addRecord(TableName.ITEMS.toString() + publicationId, itemId, ItemFamily.TFIDF.toString(),
                        word, map.get(word) + "");

        }
    }

    public static Item getItem(String itemID, String publicationId) {
        Item currentItem = new Item();

        Result rs = Utils.getOneRecord(TableName.ITEMS.toString() + publicationId, itemID);
        currentItem.setItemId(itemID);
        if(rs == null) {
            return  null;
        }
        for(KeyValue kv : rs.raw()){
            currentItem.addToItem(Enum.valueOf(ItemFamily.class, new String(kv.getFamily())),
                     new String(kv.getValue()), new String(kv.getQualifier()));
        }

        return currentItem;
    }

    public static void addItem(Item item) {
        IDF.addIdfToDatabase(item);
        String book = item.getItemId();

        if(book.length() == 0) {
            return;
        }

        Utils.addRecord(TableName.ITEMS.toString() + item.getPublicationId(), book, ItemFamily.TITLE.toString(),
                ItemFamily.TITLE.toString(), item.getTitle());
        Utils.addRecord(TableName.ITEMS.toString() + item.getPublicationId(), book, ItemFamily.SHORT_TITLE.toString(),
                ItemFamily.SHORT_TITLE.toString(), item.getShortTitle());
        Utils.addRecord(TableName.ITEMS.toString() + item.getPublicationId(), book, ItemFamily.AUTHOR.toString(),
                ItemFamily.AUTHOR.toString(), item.getAuthor());
        Utils.addRecord(TableName.ITEMS.toString() + item.getPublicationId(), book, ItemFamily.DEPARTMENT.toString(),
                ItemFamily.DEPARTMENT.toString(), item.getDepartment());
        Utils.addRecord(TableName.ITEMS.toString() + item.getPublicationId(), book, ItemFamily.CATEGORY.toString(),
                ItemFamily.CATEGORY.toString(), item.getCategory());
        Utils.addRecord(TableName.ITEMS.toString() + item.getPublicationId(), book, ItemFamily.KEYWORDS.toString(),
                ItemFamily.KEYWORDS.toString(), item.getKeywords().toArray());
        Utils.addRecord(TableName.ITEMS.toString() + item.getPublicationId(), book, ItemFamily.CONTENT.toString(),
                ItemFamily.CONTENT.toString(), item.getContent());
        Utils.addRecord(TableName.ITEMS.toString() + item.getPublicationId(), book, ItemFamily.DATE_CREATED.toString(),
                ItemFamily.DATE_CREATED.toString(), item.getDateCreated() + "");
        Utils.addRecord(TableName.ITEMS.toString() + item.getPublicationId(), book, ItemFamily.IMPORTANCE.toString(),
                ItemFamily.IMPORTANCE.toString(), item.getDateCreated() + "");
        Utils.addRecord(TableName.ITEMS.toString() + item.getPublicationId(), book, ItemFamily.PUBLICATION_ID.toString(),
                ItemFamily.PUBLICATION_ID.toString(), item.getPublicationId());
        Utils.addRecord(TableName.ITEMS.toString() + item.getPublicationId(), book, ItemFamily.LANGUAGE.toString(),
                ItemFamily.LANGUAGE.toString(), item.getLanguage());
        Utils.addRecord(TableName.ITEMS.toString() + item.getPublicationId(), book, ItemFamily.COLLECTION_REFERENCES.toString(),
                ItemFamily.COLLECTION_REFERENCES.toString(), item.getCollectionReferences().toArray());
        for(String user : item.getRating().keySet()) {
            Utils.addRecord(TableName.ITEMS.toString() + item.getPublicationId(), book, ItemFamily.RATINGS.toString(),
                    user, item.getRating().get(user) + "");
        }

    }

    public static User getUser(String userId) {
        User currentUser = new User();
        currentUser.setUserId(userId);
        Result rs = Utils.getOneRecord(TableName.USERS.toString(), userId);
        assert rs != null;
        for(KeyValue kv : rs.raw()){
            currentUser.addToUser(new String(kv.getFamily()), new String(kv.getValue()));
        }

        return currentUser;
    }

    public static LinkedList<Item> getUserItems(String userId, String publicationId) {
        User user = getUser(userId);
        LinkedList<Item> itemList = new LinkedList<Item>();

        for(String itemId : user.getItemsHistory()) {
            itemList.add(getItem(itemId, publicationId));
        }

        return  itemList;
    }

    public static LinkedList<Item> getUserRatedItems(String userId, String publicationId) {
        User user = getUser(userId);
        LinkedList<Item> itemList = new LinkedList<Item>();

        for(String itemId : user.getItemsHistory()) {
            Item item = getItem(itemId, publicationId);
            if (item.getRating().containsKey(userId)) {
                itemList.add(item);
            }
        }

        return  itemList;
    }

    public static LinkedListWrapper<Item> getRelatedArticles (String articleId, int maxArticle, String publicationId,
                                                              boolean useCollaborativeFiltering,
                                                              SimilarityWeights similarityWeights) {


        return new LinkedListWrapper<Item>(RelatedArticles.recommend(articleId, maxArticle,
                useCollaborativeFiltering, publicationId, similarityWeights));

    }

    public static LinkedListWrapper<Item> getRecommendedArticles(String userId, int maxArticle, String publicationId) {
        return new LinkedListWrapper<Item>(RecommendedArticles.recommend(userId, maxArticle, publicationId));
    }

    public static LinkedListWrapper<Item> getFriendDirectlyRecommendedArticles(String userId, int maxArticles,
                                                                               String publicationId) {
        LinkedList<String> recommendedItem =  Database.getUser(userId).getTopFriends();
        LinkedList<Item> recommendedItemList = new LinkedList<Item>();

        for(String friend : recommendedItem) {
            LinkedList<String> itemList = Database.getUser(friend).getItemsDirectlyRecommended();

            for(String item : itemList) {
                recommendedItemList.add(Database.getItem(item, publicationId));
                if(recommendedItemList.size() >= maxArticles) {
                    return new LinkedListWrapper<Item>(recommendedItemList);
                }
            }
        }

        return new LinkedListWrapper<Item>(recommendedItemList);
    }

    public static boolean addUser(User user) {
        Utils.addRecord(TableName.USERS.toString(), user.getUserId(), UserFamily.ITEM_HISTORY.toString(),
                UserFamily.ITEM_HISTORY.toString(), user.getItemsHistory().toArray());
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

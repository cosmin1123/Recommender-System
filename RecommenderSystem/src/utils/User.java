package utils;

import database.UserFamily;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by didii on 2/24/15.
 */
public class User {
    private String userId;
    private LinkedList<String> topCategories;
    private LinkedList<String> topFriends;
    private LinkedList<String> itemsHistory;
    private LinkedList<String> itemsDirectlyRecommended;
    private LinkedList<String> itemsRecommended;

    public User(String userId, LinkedList<String> topCategories,
                LinkedList<String> topFriends, LinkedList<String> itemsHistory,
                LinkedList<String> itemsDirectlyRecommended, LinkedList<String> itemsRecommended) {
        this.userId = userId;
        this.topCategories = topCategories;
        this.topFriends = topFriends;
        this.itemsHistory = itemsHistory;
        this.itemsDirectlyRecommended = itemsDirectlyRecommended;
        this.itemsRecommended = itemsRecommended;
    }

    public User() {
        this.userId = "";
        this.topCategories = new LinkedList<String>();
        this.topCategories = new LinkedList<String>();
        this.itemsHistory = new LinkedList<String>();
        this.itemsDirectlyRecommended = new LinkedList<String>();
        this.itemsRecommended = new LinkedList<String>();
        this.topFriends = new LinkedList<String>();
    }

    public void addToUser(String familyName, String value) {
        if (familyName.equals(UserFamily.ITEM_HISTORY.toString())) {
            this.itemsHistory.addAll(Arrays.asList(value.split(";")));
        }

        if (familyName.equals(UserFamily.ITEMS_RECOMMENDED.toString())) {
            this.itemsRecommended.addAll(Arrays.asList(value.split(";")));
        }

        if (familyName.equals(UserFamily.ITEMS_RECOMMENDED_DIRECTLY.toString())) {
            this.itemsDirectlyRecommended.addAll(Arrays.asList(value.split(";")));
        }

        if (familyName.equals(UserFamily.PREFERRED_CATEGORIES.toString())) {
            this.topCategories.addAll(Arrays.asList(value.split(";")));
        }

        if (familyName.equals(UserFamily.TOP_FRIENDS.toString())) {
            this.topFriends.addAll(Arrays.asList(value.split(";")));

        }


    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LinkedList<String> getTopCategories() {
        return this.topCategories;
    }

    public LinkedList<String> getTopFriends() {
        return this.topFriends;
    }

    public LinkedList<String> getItemsHistory() {
        return this.itemsHistory;
    }

    public LinkedList<String> getItemsDirectlyRecommended() {
        return this.itemsDirectlyRecommended;
    }

    public LinkedList<String> getItemsRecommended() {
        return this.itemsRecommended;
    }
}

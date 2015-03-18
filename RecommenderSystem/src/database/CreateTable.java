package database;

import utils.Utils;

/**
 * Created by didii on 2/25/15.
 */
public class CreateTable {
    public static void intialiseTables() {
        Utils.createTable(TableName.USERS.toString(), new String[]{ UserFamily.PREFERRED_CATEGORIES.toString(),
                UserFamily.TOP_FRIENDS.toString(), UserFamily.ITEM_HISTORY.toString(),
                UserFamily.ITEMS_RECOMMENDED_DIRECTLY.toString(), UserFamily.ITEMS_RECOMMENDED.toString()});

        Utils.createTable(TableName.ITEMS.toString(),
                new String[]{ ItemFamily.NAME.toString(),
                ItemFamily.CONTENT_URL.toString(),
                ItemFamily.DATE_CREATED.toString(),
                ItemFamily.TITLE.toString(),
                ItemFamily.SHORT_TITLE.toString(),
                ItemFamily.KEYWORDS.toString(),
                ItemFamily.DEPARTMENT.toString(),
                ItemFamily.CATEGORY.toString(),
                ItemFamily.COLLECTION_REFERENCES.toString(),
                ItemFamily.AUTHOR.toString(),
                ItemFamily.RATINGS.toString(),
                ItemFamily.PUBLICATION_ID.toString(),
                ItemFamily.IMPORTANCE.toString(),
                ItemFamily.LANGUAGE.toString(),
                ItemFamily.CONTENT.toString(),
                ItemFamily.TFIDF.toString()});

        Utils.createTable(TableName.TFIDF.toString(),
                new String[] {TFIDFFamily.TOTAL_FILE_APPEARANCES.toString()});


    }

    public static void deleteAll() {
        try {
            Utils.deleteTable(TableName.ITEMS.toString());
            Utils.deleteTable(TableName.USERS.toString());
            Utils.deleteTable(TableName.TFIDF.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

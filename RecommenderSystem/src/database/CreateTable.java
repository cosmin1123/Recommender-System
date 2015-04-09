package database;

import utils.Utils;

/**
 * Created by didii on 2/25/15.
 */
public class CreateTable {

    public static void createUSERS() {
        Utils.createTable(TableName.USERS.toString(), new String[]{ UserFamily.PREFERRED_CATEGORIES.toString(),
                UserFamily.TOP_FRIENDS.toString(), UserFamily.ITEM_HISTORY.toString(),
                UserFamily.ITEMS_RECOMMENDED_DIRECTLY.toString(), UserFamily.ITEMS_RECOMMENDED.toString()});
    }

    public static void createITEMS() {
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
    }


    public static void createTFIDF() {
        Utils.createTable(TableName.TFIDF.toString(),
                new String[] {TFIDFFamily.TOTAL_FILE_APPEARANCES.toString(),
                        TFIDFFamily.TOTAL_FILE_NUM.toString()});

        Utils.addRecord(TableName.TFIDF.toString(), TFIDFFamily.TOTAL_FILE_NUM.toString(),
                TFIDFFamily.TOTAL_FILE_NUM.toString(),
                TFIDFFamily.TOTAL_FILE_NUM.toString(), "0");
    }

    public static void intialiseTables() {
        createUSERS();
        createITEMS();
        createTFIDF();
    }

    public static void deleteTFIDF() throws Exception {
        Utils.deleteTable(TableName.TFIDF.toString());
    }

    public static void deleteUSERS() throws Exception {
        Utils.deleteTable(TableName.USERS.toString());
    }

    public static void deleteITEMS() throws Exception {
        Utils.deleteTable(TableName.ITEMS.toString());
    }


    public static void deleteAll() {
        try {
            deleteITEMS();
            deleteUSERS();
            deleteTFIDF();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

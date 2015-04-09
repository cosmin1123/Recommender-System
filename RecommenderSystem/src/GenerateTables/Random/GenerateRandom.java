package GenerateTables.Random;


import database.*;
import utils.Utils;

import java.io.*;
import java.util.*;

/**
 * Created by didii on 3/3/15.
 */
public class GenerateRandom {
    static LinkedList<String> AllBookTitles = new LinkedList<String>();
    static LinkedList<String> AllCategories = new LinkedList<String>();
    static LinkedList<String> AllDepartment = new LinkedList<String>();
    static LinkedList<String> AllKeywords = new LinkedList<String>();
    static LinkedList<String> AllUserName = new LinkedList<String>();
    static LinkedList<String> AllImportance = new LinkedList<String>();
    static LinkedList<String> AllPublicationId = new LinkedList<String>();
    static LinkedList<String> AllLanguage = new LinkedList<String>();

    public static int getRandom(int max) {
        return (int) (Math.random() * max);
    }

    public static int getRandom(int min, int max) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    private static void fillUnique(LinkedList<String> list, LinkedList<String> source, int size) {
        HashSet<Integer> set = new HashSet<Integer>();

        while(set.size() <= size) {
            int rand = getRandom(source.size());
            if(!set.contains(rand)) {
                set.add(rand);
                list.add(source.get(rand));
            }
        }


    }

    private static void fillArray(BufferedReader reader, LinkedList<String> list) throws IOException {
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }

    }
    private static void fillArrays() {
        try {
            BufferedReader book = new BufferedReader(
                    (new FileReader("res/random.res/BookTitles")));
            BufferedReader category = new BufferedReader(
                    (new FileReader("res/random.res/Categories")));

            BufferedReader dep = new BufferedReader(
                    (new FileReader("res/random.res/Department")));

            BufferedReader keyword = new BufferedReader(
                    (new FileReader("res/random.res/Keywords")));

            BufferedReader userNam = new BufferedReader(
                    (new FileReader("res/random.res/UserName")));

            BufferedReader importance = new BufferedReader(
                    (new FileReader("res/random.res/Importance")));

            BufferedReader languages = new BufferedReader(
                    (new FileReader("res/random.res/Language")));

            BufferedReader publicationId = new BufferedReader(
                    (new FileReader("res/random.res/PublicationId")));

            String str = book.readLine();

            AllBookTitles.addAll(Arrays.asList(str.split(";")));
            fillArray(category, AllCategories);
            fillArray(dep, AllDepartment);
            fillArray(keyword, AllKeywords);
            fillArray(userNam, AllUserName);
            fillArray(importance, AllImportance);
            fillArray(languages, AllLanguage);
            fillArray(publicationId, AllPublicationId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fillDatabase() {
        CreateTable.deleteAll();
        CreateTable.intialiseTables();
        fillArrays();
        // add items
        for(String book : AllBookTitles) {
            if(book.equals("Final Star")) {
                System.out.println("STOP!!!");
            }
            String name = book;
            Integer dateCreated = getRandom(Integer.MAX_VALUE);
            String title = book;
            String shortTitle = book;
            String author = AllUserName.get(getRandom(AllUserName.size()));
            String department = AllDepartment.get(getRandom(AllDepartment.size()));
            String category = AllCategories.get(getRandom(AllCategories.size()));
            String importance = AllImportance.get(getRandom(AllImportance.size()));
            String publicationId = AllPublicationId.get(getRandom(AllPublicationId.size()));
            String language = AllLanguage.get(getRandom(AllLanguage.size()));

            LinkedList<String> keywords = new LinkedList<String>();
            LinkedList<String> collectionReferences = new LinkedList<String>();


            fillUnique(collectionReferences, AllCategories, getRandom(3, 20));
            fillUnique(keywords, AllKeywords, getRandom(1, 20));


            Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.IMPORTANCE.toString(),
                    ItemFamily.IMPORTANCE.toString(), importance);
            Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.LANGUAGE.toString(),
                    ItemFamily.LANGUAGE.toString(), language);
            Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.PUBLICATION_ID.toString(),
                    ItemFamily.PUBLICATION_ID.toString(), publicationId);
            Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.NAME.toString(),
                    ItemFamily.NAME.toString(), name);
            Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.DATE_CREATED.toString(),
                    ItemFamily.DATE_CREATED.toString(), dateCreated.toString());
            Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.TITLE.toString(),
                    ItemFamily.TITLE.toString(), title);
            Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.SHORT_TITLE.toString(),
                    ItemFamily.NAME.toString(), shortTitle);
            Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.AUTHOR.toString(),
                    ItemFamily.AUTHOR.toString(), author);
            Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.DEPARTMENT.toString(),
                    ItemFamily.DEPARTMENT.toString(), department);
            Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.CATEGORY.toString(),
                    ItemFamily.CATEGORY.toString(), category);
            Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.COLLECTION_REFERENCES.toString(),
                    ItemFamily.COLLECTION_REFERENCES.toString(), collectionReferences.toArray());
            Utils.addRecord(TableName.ITEMS.toString(), book, ItemFamily.KEYWORDS.toString(),
                    ItemFamily.KEYWORDS.toString(), keywords.toArray());


        }

        // add users
        for(String user : AllUserName) {
            LinkedList<String> topFriends = new LinkedList<String>();
            LinkedList<String> itemHistory = new LinkedList<String>();
            LinkedList<String> itemsRecommendedDirectly = new LinkedList<String>();

            fillUnique(topFriends, AllUserName, getRandom(10, 100));
            fillUnique(itemHistory, AllBookTitles, getRandom(110));
            fillUnique(itemsRecommendedDirectly, AllBookTitles, getRandom(itemHistory.size()));

            Utils.addRecord(TableName.USERS.toString(), user, UserFamily.TOP_FRIENDS.toString(),
                    UserFamily.TOP_FRIENDS.toString(), topFriends.toArray());
            Utils.addRecord(TableName.USERS.toString(), user, UserFamily.ITEM_HISTORY.toString(),
                    UserFamily.ITEM_HISTORY.toString(), itemHistory.toArray());
            Utils.addRecord(TableName.USERS.toString(), user, UserFamily.ITEMS_RECOMMENDED_DIRECTLY.toString(),
                    UserFamily.ITEMS_RECOMMENDED_DIRECTLY.toString(), itemsRecommendedDirectly.toArray());

            for(String item : itemHistory) {
                boolean rated = (getRandom(2) != 0);
                if(rated) {
                    Utils.addRecord(TableName.ITEMS.toString(), item, ItemFamily.RATINGS.toString(),
                            user, getRandom(1, 10) + "");
                }
            }

        }
    }

}

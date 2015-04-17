package GenerateTables.imdb;

import algorithms.related.TFIDF.TFIDF;
import com.jcraft.jsch.Buffer;
import com.kenai.jffi.Array;
import database.CreateTable;
import database.Database;
import utils.Item;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by didii on 3/20/15.
 private long dateCreated;
 private String title;
 private String shortTitle;
 private LinkedList<String> keywords;
 private String department;
 private String category;
 private String importance;
 private String publicationId;
 private String language;
 private LinkedList<String> collectionReferences;
 private String author;
 private HashMap<String, Double> ratings;
 */
public class GenerateIMDB {
    private static BufferedReader genresFile;
    private static BufferedReader keywordsFile;
    private static BufferedReader languageFile;
    private static BufferedReader moviesFile;
    private static BufferedReader plotFile;
    private static BufferedReader productionCompanyFile;
    private static BufferedReader ratingsFile;
    private static BufferedReader releaseDatesFile;

    private static boolean sameTitle(String s1, String s2) {
        String[] splitS1 = s1.split("\\{");
        String[] splitS2 = s2.split("\\{");

        return splitS2[0].contains(splitS1[0]) ||
                splitS1[0].contains(splitS2[0]);
    }

    private static void setPublishDate(Item item) throws IOException {
        String placeAndDate = getMetadataList(releaseDatesFile, item);
        long milliseconds = 0;
        if(placeAndDate.isEmpty()) {
            return;
        }
        String date = placeAndDate.split(";")[0].split(":")[1];
        SimpleDateFormat f = new SimpleDateFormat("dd MMM yyyy");
        Date d = null;
        try {
            d = f.parse(date);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        item.setDateCreated(milliseconds);
    }

    private static void setLanguage(Item item) throws IOException {
        String language = getMetadataList(languageFile, item);

        item.setLanguage(language.split(";")[0]);
    }

    private static void setRatings(Item item) throws IOException {
        ratingsFile.mark(1000);
        String currentLine = ratingsFile.readLine();

        String[] splitLine = currentLine.split("  +");

        String movieTitle = splitLine[4];

        if(!sameTitle(item.getItemId(), (movieTitle))) {
            ratingsFile.reset();
            return;
        }

        HashMap<String, Double> map = new HashMap<String, Double>();
        map.put("ZuZu", Double.parseDouble(splitLine[3]));
        item.setRatings(map);

    }

    private static void setContent(Item item) throws  IOException {
        plotFile.mark(1000);

        String currentLine = plotFile.readLine();
        String movieTitle = currentLine.substring(4, currentLine.length());

        if(!sameTitle(item.getItemId(), (movieTitle))) {
            plotFile.reset();
            return;
        }
        String plot = "";

        while (!(currentLine = plotFile.readLine()).
                equals("-------------------------------------------------------------------------------") &&
                currentLine != null) {

            if(currentLine.length() > 3) {
                plot += currentLine.substring(3, currentLine.length());
            } else {
                plot += currentLine;
            }
        }

        item.setContent(plot);
    }

    private static void setDepartment(Item item) throws IOException {
        String genres = getMetadataList(genresFile, item);
        if(genres.isEmpty()) {
            return;
        }
        String[] splitGenres = genres.split(";");

        item.setDepartment(splitGenres[0]);

        if(splitGenres.length > 1) {
            item.setCategory(splitGenres[1]);
        } else {
            item.setCategory(splitGenres[0]);
        }

        item.setCollectionReferences(Arrays.asList(splitGenres));


    }

    private static void setKeywords(Item item) throws IOException {
        String keywords = getMetadataList(keywordsFile, item);
        if(!keywords.isEmpty()) {
            item.setKeywords(keywords);
        }
    }

    private static void setAuthor(Item item) throws IOException {

        String author = getMetadataList(productionCompanyFile, item).split(";")[0];
        item.setAuthor(author);
        item.setPublicationId(author);
    }

    private static String getMetadataList(BufferedReader buffer, Item item) throws IOException {
        buffer.mark(1000);
        String currentLine = buffer.readLine();
        String data = "";

        String[] splitLine;
        do {
            if(currentLine == null) {
                break;
            }
            splitLine = currentLine.split("\t+");

            if(!sameTitle(item.getItemId(), splitLine[0])) {
                break;
            }

            data += splitLine[1] + ";";
            buffer.mark(1000);
            currentLine = buffer.readLine();
        }while(true);
        buffer.reset();

        return data;
    }

    private static boolean setItemId(Item item) throws IOException {
        String currentLine = moviesFile.readLine();
        if(currentLine == null) {
            return false;
        }

        String itemID = currentLine.split("\t")[0];

        item.setItemId(itemID);
        item.setTitle(itemID);
        item.setShortTitle(itemID);

        return true;
    }

    private static Item createItem() {
        Item newItem = new Item();
        try {
            while(setItemId(newItem)) {
                if(newItem.getItemId().contains("1714. El preu de la llibertat")) {
                    System.out.println("STOPPP");
                }
                setAuthor(newItem);
                setKeywords(newItem);
                setDepartment(newItem);
                setContent(newItem);
                setRatings(newItem);
                setLanguage(newItem);
                setPublishDate(newItem);
                Database.addItem(newItem);
                System.out.println(newItem);
                newItem = new Item();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newItem;
    }

    private static void openFiles() {
        try {
            genresFile = new BufferedReader(
                    (new FileReader("res/imdb.res/genres.list")));
            keywordsFile = new BufferedReader(
                    (new FileReader("res/imdb.res/keywords.list")));
            languageFile = new BufferedReader(
                    (new FileReader("res/imdb.res/language.list")));
            moviesFile = new BufferedReader(
                    (new FileReader("res/imdb.res/movies.list")));
            plotFile = new BufferedReader(
                    (new FileReader("res/imdb.res/plot.list")));
            productionCompanyFile = new BufferedReader(
                    (new FileReader("res/imdb.res/production-companies.list")));
            ratingsFile = new BufferedReader(
                    (new FileReader("res/imdb.res/ratings.list")));
            releaseDatesFile = new BufferedReader(
                    (new FileReader("res/imdb.res/release-dates.list")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void closeFiles() {
        try {
            genresFile.close();
            keywordsFile.close();
            languageFile.close();
            moviesFile.close();
            plotFile.close();
            productionCompanyFile.close();
            ratingsFile.close();
            releaseDatesFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fillDatabase() {

        CreateTable.deleteAll();
        CreateTable.intialiseTables();

        openFiles();
        createItem();
        closeFiles();

        TFIDF.computeForAll();
    }
}

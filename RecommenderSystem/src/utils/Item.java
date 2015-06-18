package utils;

import database.ItemFamily;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.PDFTextStripper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by didii on 2/24/15.
 *     NAME, CONTENT_URL, DATE_CREATED, TITLE, SHORT_TITLE, KEYWORDS, DEPARTMENT, CATEGORY,
 COLLECTION_REFERENCES, AUTHOR, RATINGS
 */
public class Item {
    private String itemId;
    private double score;
    private String contentUrl;
    private String content;
    private long dateCreated;
    private String name;
    private String title;
    private String shortTitle;
    private LinkedList<String> keywords;
    private String department;
    private String category;
    private String importance;
    private String publicationId;
    private String language;
    private List<String> collectionReferences;
    private String author;
    private HashMap<String, Double> ratings;
    private HashMap<String, Double> tfidf;

    private LinkedList<String> relatedArticles;

    public Item() {
        itemId = "";
        contentUrl = "";
        title = "";
        shortTitle = "";
        keywords = new LinkedList<String>();
        department = "";
        category = "";
        collectionReferences = new LinkedList<String>();
        author = "";
        ratings = new HashMap<String, Double>();
        tfidf = new HashMap<String, Double>();
        this.importance = "";
        this.publicationId = "";
        this.language = "";
        this.content = "";
    }

    public void addToItem(ItemFamily family, String value, String qualifiers) {
        if(ItemFamily.AUTHOR.equals(family)) {
            this.author = value;
        }

        if(ItemFamily.RATINGS.equals(family)) {
            this.ratings.put(qualifiers, new Double(value));
        }

        if(ItemFamily.COLLECTION_REFERENCES.equals(family)) {
            this.collectionReferences.addAll(Arrays.asList(value.split(";")));
        }

        if(ItemFamily.CATEGORY.equals(family)) {
            this.category = value;
        }

        if(ItemFamily.CONTENT_URL.equals(family)) {
            this.contentUrl = value;
        }

        if(ItemFamily.DATE_CREATED.equals(family)) {
            this.dateCreated = new Long(value);
        }

        if(ItemFamily.DEPARTMENT.equals(family)) {
            this.department = value;
        }

        if(ItemFamily.KEYWORDS.equals(family)) {
            this.keywords.addAll(Arrays.asList(value.split(";")));
        }

        if(ItemFamily.NAME.equals(family)) {
            this.name = value;
        }

        if(ItemFamily.TITLE.equals(family)) {
            this.title = value;
        }
        if(ItemFamily.SHORT_TITLE.equals(family)) {
            this.shortTitle = value;
        }

        if(ItemFamily.IMPORTANCE.equals(family)) {
            this.importance = value;
        }

        if(ItemFamily.PUBLICATION_ID.equals(family)) {
            this.publicationId = value;
        }

        if(ItemFamily.LANGUAGE.equals(family)) {
            this.language = value;
        }

        if(ItemFamily.CONTENT.equals(family)) {
            this.content = value;
        }

        if(ItemFamily.TFIDF.equals(family)) {
            this.tfidf.put(qualifiers, new Double(value));
        }
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public void setKeywords(String keywords) {
        this.keywords.addAll(Arrays.asList(keywords.split(";")));
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = Long.parseLong(dateCreated);
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }


    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setRatings(HashMap<String, Double> ratings) {
        this.ratings = ratings;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }
    public void setPublicationId(String publicationId) {
        this.publicationId = publicationId;
    }
    public void setLanguage(String language) { this.language = language;}
    public void setCollectionReferences(List<String> collectionReferences) {
        this.collectionReferences = collectionReferences;
    }

    public String getPublicationId() {
        return this.publicationId;
    }


    public String getLanguage() {
        return this.language;
    }

    public String getImportance() {
        return this.importance;
    }

    public List<String> getCollectionReferences() {
        return this.collectionReferences;
    }

    public HashMap<String, Double> getTfidf() {
        return this.tfidf;
    }
    public String getContent() {
        return this.content;
    }
    public String getDepartment() {
        return this.department;
    }
    public LinkedList<String> getKeywords() {
       return this.keywords;
    }

    public String getShortTitle() {
        return this.shortTitle;
    }

    public String getTitle() {
        return this.title;
    }

    public String getName() {
        return this.name;
    }

    public String getContentUrl() {
        return this.contentUrl;
    }

    public LinkedList<String> getRelatedArticles() {
        return this.relatedArticles;
    }

    public String getItemId() {
        return this.itemId;
    }
    public String getCategory() {
        return this.category;
    }
    public HashMap<String, Double> getRating() {
        return this.ratings;
    }
    public long getDateCreated() {
        return this.dateCreated;
    }
    public String getAuthor() {
        return this.author;
    }


    public boolean equals(Object obj) {
        if(obj instanceof  Item) {
            return this.itemId.equals(((Item)obj).getItemId());
        }

        return obj.equals(this);

    }

    @Override
    public String toString() {
        String out = "";

        out += this.itemId               + "\n";
        out += this.title                + "\n";
        out += this.dateCreated          + "\n";
        out += this.shortTitle           + "\n";
        out += this.content              + "\n";
        out += this.keywords             + "\n";
        out += this.category             + "\n";
        out += this.department           + "\n";
        out += this.author               + "\n";
        out += this.collectionReferences + "\n";

        return out;

    }
    @Override
    public int hashCode() {
        return this.itemId.hashCode();
    }

    public String getValue(JSONObject object, String key) {
        if(object.has(key)) {
            try {
                String value = (object.getString(key));
                return value;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return "";

    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean fillItemFromJson(String JSONArticle) {
        if(JSONArticle.equals("undefined")) {
            return false;
        }
        try {
            JSONObject articleObject = new JSONObject(JSONArticle);

            String abstractText = getValue(articleObject, "abstract");
            String author = getValue(articleObject, "author");
            String department = getValue(articleObject, "department");

            if(articleObject.has("keywords")) {
                JSONArray array = articleObject.getJSONArray("keywords");
                int n = array.length();
                for (int i = 0; i < n; i++) {
                    String keyWord = array.getString(i);
                    this.keywords.add(keyWord);
                }
            }
            String createdDate = getValue(articleObject,"created");
            String title = getValue(articleObject,"title");
            String shortTitle = getValue(articleObject,"shortTitle");
            String importance = getValue(articleObject,"importance");
            String entityId = getValue(articleObject, "entityId");
            String articleId = getValue(articleObject, "articleId");
            String publicationId = getValue(articleObject,"publicationId");

            this.author = author;
            this.department = department;
            //this.content = abstractText;
            this.title = title;
            this.shortTitle = shortTitle;
            this.importance = importance;
            this.itemId = entityId + articleId;
            this.publicationId = publicationId;

            SimpleDateFormat f = new SimpleDateFormat("yyyy-mm-dd");
            Date d = f.parse(createdDate.substring(0, createdDate.indexOf("T")));
            long milliseconds = d.getTime();
            this.setDateCreated(milliseconds);

            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void parsePdfByteStream(byte[] byteArray) {
        if(byteArray == null || byteArray.length == 0){
            return;
        }
        PDFParser parser;
        String parsedText = "";
        PDFTextStripper pdfStripper;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;


        InputStream inputStream = new ByteArrayInputStream(byteArray);
        try {
            parser = new PDFParser(inputStream);
        } catch (Exception e) {
            System.out.println("Unable to open PDF Parser.");
            return;
        }

        try {
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            parsedText = pdfStripper.getText(pdDoc);
            pdDoc.close();
            cosDoc.close();
        } catch (Exception e) {
            System.out.println("An exception occured in parsing the PDF Document.");
            e.printStackTrace();
            try {
                if (cosDoc != null) cosDoc.close();
                if (pdDoc != null) pdDoc.close();
            } catch (Exception e1) {
                e.printStackTrace();
            }
            return;
        }
        this.content = parsedText;

    }
}

package GenerateTables.fastCompany;

import algorithms.related.TFIDF.TFIDF;
import database.CreateTable;
import database.Database;
import org.apache.hadoop.hbase.util.Hash;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import utils.Item;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by didii on 3/17/15.
 * itemId = "";
 title = "";
 shortTitle = "";
 keywords;
 department;
 category;
 author = "";
 this.content = "";
 dateCreated;
 */
public class GenerateFastCompany {





    public static void fillDatabase() {
        CreateTable.deleteAll();
        CreateTable.intialiseTables();
        int threadNum = 16;
        Thread []getThreads = new Thread[threadNum];

        int start = 3000000;
        int end = 3043700;

        int total = end - start;


        for(int i = 0; i < threadNum; i++) {
            int threadStart = i * (total / threadNum) + start;
            int threadEnd = (i + 1) * (total / threadNum) + start;
            if(i == (threadNum - 1)) {
                threadEnd = end;
            }
            getThreads[i] = new GetThread(threadStart, threadEnd );
            getThreads[i].start();

        }

        for(int i = 0; i < threadNum; i++) {
            try {
                getThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

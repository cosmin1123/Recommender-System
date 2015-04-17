package utils;

import java.io.IOException;

import database.AllItemsMapper;
import database.ItemFamily;
import database.TableName;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.*;


import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobPriority;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapreduce.Job;


import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 * Created by didii on 2/25/15.
 */
public class Utils {
    private static  Configuration conf = null;
    private static HTable itemTable = null;
    private static HTable userTable = null;
    private static HTable tfidfTable = null;

    private static Object itemTableSynch = null;
    private static Object userTableSynch = null;
    private static Object tfidfTableSynch = null;

    /**
     * Initialization
     */
    static {
        conf = HBaseConfiguration.create();
        try {
            itemTable = new HTable(conf, TableName.ITEMS.toString());
            userTable = new HTable(conf, TableName.USERS.toString());
            tfidfTable = new HTable(conf, TableName.TFIDF.toString());
            itemTableSynch = new Object();
            userTableSynch = new Object();
            tfidfTableSynch = new Object();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void createTable(String tableName, String[] families) {

        HBaseAdmin admin;
        try {
                admin = new HBaseAdmin(conf);

            if (admin.tableExists(tableName)) {
                System.out.println("table already exists!");
            } else {
                HTableDescriptor tableDesc = new HTableDescriptor(tableName);
                for (int i = 0; i < families.length; i++) {
                    tableDesc.addFamily(new HColumnDescriptor(families[i]));
                }
                admin.createTable(tableDesc);
                System.out.println("create table " + tableName + " ok.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a table
     */
    public static void deleteTable(String tableName) throws Exception {
        try {
            HBaseAdmin admin = new HBaseAdmin(conf);
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
            System.out.println("delete table " + tableName + " ok.");
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        }
    }

    private static void putInTable( HTable table, Object sync, Put put)
            throws InterruptedIOException, RetriesExhaustedWithDetailsException {

        synchronized (sync) {
            table.put(put);
        }
    }

    /**
     * Put (or insert) a row
     */
    public static void addRecord(String tableName, String rowKey,
                                 String family, String qualifier, String value) {
        try {

            Put put = new Put(Bytes.toBytes(rowKey));
            put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes
                    .toBytes(value));

            if(tableName.equals(TableName.ITEMS.toString())) {
                putInTable( itemTable, itemTableSynch, put);
            } else {
                if(tableName.equals(TableName.USERS.toString())) {
                    putInTable( userTable, userTableSynch, put);
                } else {
                    if(tableName.equals(TableName.TFIDF.toString())) {
                        putInTable(tfidfTable, tfidfTableSynch, put);
                    }
                }

            }

            System.out.println("inserted record " + rowKey + " to table "
                    + tableName + " ok.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Put (or insert) a list row
     */
    public static void addRecord(String tableName, String rowKey,
                                     String family, String qualifier, Object value[]) {
        try {
            Put put = new Put(Bytes.toBytes(rowKey));
            String val = "";
            for(int i = 0; i < value.length; i++) {
                val += value[i] + ";";
            }
            put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes
                    .toBytes(val));

            if(tableName.equals(TableName.ITEMS.toString())) {
                putInTable(itemTable, itemTableSynch, put);
            } else {
                putInTable(userTable, userTableSynch, put);
            }

            System.out.println("inserted record " + rowKey + " to table "
                    + tableName + " ok.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a row
     */
    public static void delRecord(String tableName, String rowKey)
            throws IOException {
        HTable table = new HTable(conf, tableName);
        List<Delete> list = new ArrayList<Delete>();
        Delete del = new Delete(rowKey.getBytes());
        list.add(del);
        table.delete(list);
        System.out.println("del record " + rowKey + " ok.");
    }

    /**
     * Get a row
     */
    public static Result getOneRecord (String tableName, String rowKey){
        HTable table;
        try {
            table = new HTable(conf, tableName);

            Get get = new Get(rowKey.getBytes());
            return table.get(get);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * Scan (or list) a table
     */
    public static LinkedList<User> getAllUsers (String tableName) {
        try{
            HTable table = new HTable(conf, tableName);
            Scan s = new Scan();
            ResultScanner ss = table.getScanner(s);
            LinkedList<User> list = new LinkedList<User>();
                for (Result r : ss) {
                    User user = new User();
                    for (KeyValue kv : r.raw()) {
                        user.addToUser(new String(kv.getFamily()), new String(kv.getValue()));
                        user.setUserId(new String(kv.getRow()));
                    }
                    list.add(user);
                }


            return list;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Scan (or list) a table
     */
    public static LinkedList<Item> getAllItems (String tableName) {
        try{

            if(!AllItemsMapper.list.isEmpty()) {
                return AllItemsMapper.list;
            }

            boolean b = false;
            Job [] jobArray = new Job[5];
            for(int i = 0; i <= 4; i++) {
                jobArray[i] = AllItemsMapper.startNewMapper(conf, "30" + i + "0000", "30" + (i + 1) + "0000");
            }

            for(int i = 0; i <= 4; i++) {
                // TODO with sleep and things
                while (!jobArray[i].isComplete());

            }
            return AllItemsMapper.list;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}

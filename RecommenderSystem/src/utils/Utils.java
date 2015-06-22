package utils;

import database.AllItemsMapper;
import database.TableName;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by didii on 2/25/15.
 */
public class Utils {
    private static Configuration conf = null;
    private static HTable itemTable = null;
    private static HTable userTable = null;
    private static HTable tfidfTable = null;

    private static Object itemTableSynch = null;
    private static Object userTableSynch = null;
    private static Object tfidfTableSynch = null;
    private static HBaseAdmin admin;
    private static HashMap<String, HTable> map;

    /**
     * Initialization
     */
    static {
        conf = HBaseConfiguration.create();
        try {
            map = new HashMap<String, HTable>();
            itemTable = new HTable(conf, TableName.ITEMS.toString());
            userTable = new HTable(conf, TableName.USERS.toString());
            tfidfTable = new HTable(conf, TableName.TFIDF.toString());
            admin = new HBaseAdmin(conf);

            map.put("", itemTable);

            itemTableSynch = new Object();
            userTableSynch = new Object();
            tfidfTableSynch = new Object();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean tableExists(String tableName) {
        try {
            return admin.tableExists(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void createTable(String tableName, String[] families) {

        try {


            if (admin.tableExists(tableName)) {
                System.out.println("table already exists!");
            } else {
                HTableDescriptor tableDesc = new HTableDescriptor(tableName);
                for (String family : families) {
                    tableDesc.addFamily(new HColumnDescriptor(family));
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

    private static void putInTable(HTable table, Object sync, Put put)
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
            if (rowKey.length() == 0) {
                return;
            }
            Put put = new Put(Bytes.toBytes(rowKey));
            put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes
                    .toBytes(value));

            if (tableName.contains(TableName.ITEMS.toString())) {
                if (map.get(tableName) == null) {
                    map.put(tableName, new HTable(conf, tableName));
                }
                putInTable(map.get(tableName), itemTableSynch, put);
            } else {
                if (tableName.contains(TableName.USERS.toString())) {
                    putInTable(userTable, userTableSynch, put);
                } else {
                    if (tableName.contains(TableName.TFIDF.toString())) {
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
            if (rowKey.length() == 0) {
                return;
            }
            Put put = new Put(Bytes.toBytes(rowKey));
            String val = "";
            for (Object aValue : value) {
                val += aValue + ";";
            }
            put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes
                    .toBytes(val));

            if (tableName.contains(TableName.ITEMS.toString())) {
                if (map.get(tableName) == null) {
                    map.put(tableName, new HTable(conf, tableName));
                }
                putInTable(map.get(tableName), itemTableSynch, put);
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
    public static Result getOneRecord(String tableName, String rowKey) {
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
    public static LinkedList<User> getAllUsers(String tableName) {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Scan (or list) a table
     */
    public static LinkedList<Item> getAllItems(String tableName, String publicationId) {
        try {

            if (!AllItemsMapper.list.isEmpty()) {
                return AllItemsMapper.list;
            }

            boolean b = false;
            Job job = AllItemsMapper.startNewMapper(conf,
                    tableName, publicationId);


            while (!job.isComplete()) ;


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

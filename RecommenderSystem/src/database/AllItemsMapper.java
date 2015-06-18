package database;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import utils.Item;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by didii on 3/27/15.
 */
public class AllItemsMapper extends TableMapper<ImmutableBytesWritable, Put>  {
    public static final LinkedList<Item> list = new LinkedList<Item>();

    public static Job startNewMapper(Configuration conf, String tableName,
                                     String publicationID)
            throws InterruptedException, IOException, ClassNotFoundException {
        Scan s = new Scan();

        JobConf config = new JobConf(conf);

        config.setUseNewMapper(true);



        Job job = new Job(config, "Read");

        job.setJarByClass(AllItemsMapper.class);     // class that contains mapper



        TableMapReduceUtil.initTableMapperJob(
                tableName + publicationID,        // input HBase table name
                s,             // Scan instance to control CF and attribute selection
                AllItemsMapper.class,   // mapper
                null,             // mapper output key
                null,             // mapper output value
                job);


        job.setOutputFormatClass(org.apache.hadoop.mapreduce.lib.output.NullOutputFormat.class);
        job.submit();
        return job;
    }

    public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
        Item item = new Item();

        for (KeyValue kv : value.raw()) {
            item.addToItem(Enum.valueOf(ItemFamily.class,new String(kv.getFamily())),
                    new String(kv.getValue()), new String(kv.getQualifier()));

            item.setItemId(new String(kv.getRow()));
        }

        synchronized (AllItemsMapper.class) {
            list.add(item);
        }
    }
}

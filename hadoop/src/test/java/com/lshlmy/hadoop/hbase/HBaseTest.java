package com.lshlmy.hadoop.hbase;

import com.lshlmy.hadoop.Application;
import com.lshlmy.hadoop.hbase.dao.MyHBaseDAO;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigInteger;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class HBaseTest {

    private static final Logger logger = LoggerFactory.getLogger(HBaseTest.class);
    private static final String CF_DEFAULT = "DEFAULT_COLUMN_FAMILY";

    @Autowired
    private MyHBaseDAO myHBaseDAO;

    static {
        System.setProperty("hadoop.home.dir", "D:\\文档\\hadoop");
        System.setProperty("HADOOP_USER_NAME", "lshlmy");
    }

    public static boolean createTable(HBaseAdmin admin, HTableDescriptor table, byte[][] splits)
            throws IOException {
        try {
            admin.createTable(table, splits);
            return true;
        } catch (TableExistsException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static byte[][] getHexSplits(String startKey, String endKey, int numRegions) {
        byte[][] splits = new byte[numRegions - 1][];
        BigInteger lowestKey = new BigInteger(startKey, 16);
        BigInteger highestKey = new BigInteger(endKey, 16);
        BigInteger range = highestKey.subtract(lowestKey);
        BigInteger regionIncrement = range.divide(BigInteger.valueOf(numRegions));
        lowestKey = lowestKey.add(regionIncrement);
        for (int i = 0; i < numRegions - 1; i++) {
            BigInteger key = lowestKey.add(regionIncrement.multiply(BigInteger.valueOf(i)));
            byte[] b = String.format("%016x", key).getBytes();
            splits[i] = b;
        }
        return splits;
    }


    @Test
    public void test1() {
        Configuration conf = HBaseConfiguration.create();
        String[] cfs = new String[]{"events"};
        TableName tableName = TableName.valueOf("test");
        try (Connection conn = ConnectionFactory.createConnection(conf); Admin admin =  conn.getAdmin()) {
            if (admin.tableExists(tableName)) {
                logger.warn("Table: {} is exists!", tableName);
                return;
            }
            HTableDescriptor tableDesc = new HTableDescriptor(tableName);
            for (int i = 0; i < cfs.length; i++) {
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cfs[i]);
                hColumnDescriptor.setCompressionType(Compression.Algorithm.SNAPPY);
                hColumnDescriptor.setMaxVersions(1);
                tableDesc.addFamily(hColumnDescriptor);
            }
            String[] s = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
            int partition = 16;
            byte[][] splitKeys = new byte[partition - 1][];
            for (int i = 1; i < partition; i++) {
                splitKeys[i - 1] = Bytes.toBytes(s[i - 1]);
            }
            admin.createTable(tableDesc, splitKeys);
            logger.info("Table: {} create success!", tableName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Test
    public void test(){
        String result = myHBaseDAO.get("member", "scutshuxue", "info", "age");
        System.out.println("result:"+result);
    }
}

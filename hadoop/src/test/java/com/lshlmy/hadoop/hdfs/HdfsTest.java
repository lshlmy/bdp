package com.lshlmy.hadoop.hdfs;

import com.lshlmy.hadoop.hdfs.util.HdfsUtils;
import com.lshlmy.hadoop.pool.ConnectionHdfsFactory;
import com.lshlmy.hadoop.pool.ConnectionHdfsPool;
import org.apache.commons.pool2.BaseObjectPool;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.BaseGenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HdfsTest {

    private static Configuration configuration = new Configuration();


    public static void  main(String[] args) throws Exception {
        System.setProperty("hadoop.home.dir", "D:/文档/hadoop");
        String uri = "hdfs://192.168.0.222:9000/test/test1.txt";

        DistributedFileSystem fileSystem = (DistributedFileSystem) HdfsUtils.get();

        try (InputStream inputStream = fileSystem.open(new Path(uri)); ByteArrayOutputStream outSteam = new ByteArrayOutputStream()){
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1){
                outSteam.write(buffer);
            }
            System.out.println(outSteam.toString("UTF-8"));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(fileSystem);
            HdfsUtils.returnFileSystem(fileSystem);
        }
    }

   public class Task implements Callable{
        @Override
        public String call() throws Exception {
            return "";
        }
    }

    @Test
    public void test2() throws Exception {



    }
}

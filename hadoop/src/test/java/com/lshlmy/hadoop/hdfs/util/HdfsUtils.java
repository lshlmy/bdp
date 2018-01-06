package com.lshlmy.hadoop.hdfs.util;

import com.lshlmy.hadoop.pool.ConnectionHdfsPool;
import org.apache.commons.io.IOUtils;
import org.apache.commons.pool2.ObjectPool;
import org.apache.hadoop.fs.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HdfsUtils {

    private static final Logger logger = LoggerFactory.getLogger(HdfsUtils.class);
    private static ObjectPool<FileSystem> pool = new ConnectionHdfsPool();


    static {

    }

    private HdfsUtils() {
    }

    /**
     * 获取文件系统
     *
     * @return
     */
    public static FileSystem get() {
        FileSystem fs = null;
        try {
            fs = pool.borrowObject();
        } catch (Exception e) {
            logger.error("获取文件系统失败", e);
        }
        return fs;
    }

    /**
     * 将文件系统对象放回对象池
     *
     * @param fs
     */
    public static void returnFileSystem(FileSystem fs) {
        try {
            pool.returnObject(fs);
        } catch (Exception e) {
            logger.error("将文件系统对象放回对象池失败", e);
            throw new RuntimeException();
        }
    }

}

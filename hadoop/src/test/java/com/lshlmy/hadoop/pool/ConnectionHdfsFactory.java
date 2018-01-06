package com.lshlmy.hadoop.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

public class ConnectionHdfsFactory extends BasePooledObjectFactory<FileSystem> {

    private static final Configuration configuration = new Configuration();

    public ConnectionHdfsFactory() {
    }

    @Override
    public FileSystem create() throws Exception {
        return FileSystem.get(configuration);
    }

    @Override
    public PooledObject<FileSystem> wrap(FileSystem obj) {
        return new DefaultPooledObject<>(obj);
    }
}

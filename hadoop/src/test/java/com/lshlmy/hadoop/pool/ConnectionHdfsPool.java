package com.lshlmy.hadoop.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.hadoop.fs.FileSystem;


public class ConnectionHdfsPool extends GenericObjectPool<FileSystem> {

    public ConnectionHdfsPool() {
        super(new ConnectionHdfsFactory(), new ConnectionHdfsPoolConfig());
    }

    public ConnectionHdfsPool(GenericObjectPoolConfig poolConfig) {
        super(new ConnectionHdfsFactory(), poolConfig);
    }

    @Override
    public void returnObject(FileSystem fileSystem) {
        super.returnObject(fileSystem);
    }
}
package com.lshlmy.hadoop.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 连接池配置
 */
public class ConnectionHdfsPoolConfig extends GenericObjectPoolConfig {

    public ConnectionHdfsPoolConfig() {
        setMinIdle(5);
        setTestOnBorrow(true);
    }
}

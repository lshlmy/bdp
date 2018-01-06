package com.lshlmy.hadoop.hbase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

@Configuration
public class HbaseConfig{


    @Bean("htemplate")
    public HbaseTemplate getHbaseTemplate(org.apache.hadoop.conf.Configuration hadoopConfiguration){
        return new HbaseTemplate(hadoopConfiguration);
    }
}

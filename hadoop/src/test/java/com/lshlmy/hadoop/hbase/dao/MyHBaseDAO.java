package com.lshlmy.hadoop.hbase.dao;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MyHBaseDAO {

    @Autowired
    private HbaseTemplate htemplate;


    public String get(String tableName, String rowName, String familyName, String qualifier) {
        return htemplate.get(tableName, rowName, familyName, qualifier,
                (Result result, int rowNum) -> {
                    List<Cell> ceList = result.listCells();
                    String res = "";
                    if (ceList != null && ceList.size() > 0) {
                        for (Cell cell : ceList) {
                            res = Bytes.toString(cell.getValueArray(),
                                    cell.getValueOffset(),
                                    cell.getValueLength());
                        }
                    }
                    return res;
                });

    }
}

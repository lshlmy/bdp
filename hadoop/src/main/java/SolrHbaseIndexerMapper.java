import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class SolrHbaseIndexerMapper extends TableMapper<Text, Text> {

    private static final Logger logger = LoggerFactory.getLogger(SolrHbaseIndexerMapper.class);

    private static final String solrUrl = "http://192.168.0.222:8983/solr";
    private static final String solrCoreName = "c_cons";

    @Override
    protected void map(final ImmutableBytesWritable key, Result value, Context context) {
        logger.info("开始执行map任务");
        try {
            if (value != null) {
                List<Cell> cells = value.listCells();
                if (CollectionUtils.isNotEmpty(value.listCells())) {
                    SolrClient solrClient = new HttpSolrClient.Builder(solrUrl).withConnectionTimeout(10000).withSocketTimeout(60000).build();
                    SolrInputDocument doc = new SolrInputDocument();

                    cells.forEach((cell) -> {
                        String columnName = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                        String columnValue = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                        logger.info("id：{},字段名称：{}，字段值：{}", Bytes.toString(key.get()), columnName, columnValue);
                        //doc.addField(columnName, columnValue);
                    });
                try {
                    solrClient.add(solrCoreName, doc);
                    solrClient.commit();
                } catch (SolrServerException e) {
                    e.printStackTrace();
                }
                }
            }
        } catch (Exception e) {
            logger.error("map任务错误", e);
        }
        logger.info("map任务结束");
    }
}

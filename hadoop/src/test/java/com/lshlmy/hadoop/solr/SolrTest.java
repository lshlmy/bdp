package com.lshlmy.hadoop.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.MapSolrParams;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SolrTest {

    private static final String solrUrl = "http://192.168.0.222:8983/solr";
    private static final String solrCoreName = "c_cons";

    public static SolrClient getSolrClient() {
        return new HttpSolrClient.Builder(solrUrl)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
    }

    @Test
    public void getResult() {
        final SolrClient client = getSolrClient();

        final Map<String, String> queryParamMap = new HashMap<String, String>();
        queryParamMap.put("q", "*:*");
        MapSolrParams queryParams = new MapSolrParams(queryParamMap);

        final QueryResponse response;
        try {
            response = client.query(solrCoreName, queryParams);
            final SolrDocumentList documents = response.getResults();

            for (SolrDocument document : documents) {
                System.out.println(document.getFieldValueMap());
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void setIndex() {
        final SolrClient client = getSolrClient();

        final SolrInputDocument doc = new SolrInputDocument();
        doc.addField("rowkey", "123456");

        try {
            final UpdateResponse updateResponse = client.add(solrCoreName, doc);
            client.commit(solrCoreName);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

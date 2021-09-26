package cn.lqso.es.service;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.lqso.es.bean.EsConfig;
import cn.lqso.es.model.AccountInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * es服务
 * @author luojie
 * @since 2018-12-27
 */
@Service
public class EsService {
    protected Logger logger = LoggerFactory.getLogger(EsService.class);

    @Autowired
    private EsConfig config;

    public List<AccountInfo> queryAccountInfo(String yhmc, String yddz, String dqbm, boolean isResident, int size) {
        List<AccountInfo> lst = Lists.newArrayList();
        // get client
        RestHighLevelClient client = getClient();

        // SearchRequest
        SearchRequest searchRequest = new SearchRequest();
        // SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // BoolQueryBuilder
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if(StringUtils.isNotEmpty(yhmc)){
            boolQueryBuilder.must(QueryBuilders.matchQuery("yhmc", yhmc));
        }
        if(StringUtils.isNotEmpty(yddz)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("yddz", yddz));
        }
        boolQueryBuilder.must(QueryBuilders.termQuery("dqbm", dqbm));
        if(isResident){
            boolQueryBuilder.must(QueryBuilders.termQuery("ydlbdm", 500));
        }else{
            boolQueryBuilder.mustNot(QueryBuilders.termQuery("ydlbdm", 500));
        }
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(size);
        searchSourceBuilder.timeout(new TimeValue(30, TimeUnit.SECONDS));

        searchRequest.indices(config.getIndex());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error("client.search IOException:", e);
        }

        if(searchResponse == null){
            return lst;
        }

        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            // yhbh, yhmc, yddz, ydlbdm, dqbm, gddwbm
            Map<String, Object> map = hit.getSourceAsMap();
            AccountInfo info = new AccountInfo();
            info.setYhbh(String.valueOf(map.get("yhbh")));
            info.setYhmc(String.valueOf(map.get("yhmc")));
            info.setYddz(String.valueOf(map.get("yddz")));
            info.setYdlbdm(String.valueOf(map.get("ydlbdm")));
            info.setDqbm(String.valueOf(map.get("dqbm")));
            info.setGddwbm(String.valueOf(map.get("gddwbm")));
            lst.add(info);
        }

        try {
            client.close();
        } catch (IOException e) {
            logger.error("client.close IOException:", e);
        }
        return lst;
    }

    private RestHighLevelClient getClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(config.getHost(), config.getPort(), "http")));
        return client;
    }
}

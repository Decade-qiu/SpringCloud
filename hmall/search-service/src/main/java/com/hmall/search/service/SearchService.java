package com.hmall.search.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Page;
import cn.hutool.json.JSONUtil;
import com.hmall.api.dto.ItemDTO;
import com.hmall.search.domain.DTO.PageDTO;
import com.hmall.common.utils.CollUtils;
import com.hmall.search.domain.po.ItemDoc;
import com.hmall.search.domain.query.ItemPageQuery;
import com.hmall.search.domain.vo.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final RestHighLevelClient client;

    private final String INDEX = "items";

    public PageDTO<ItemDoc> search(ItemPageQuery query) {
        SearchRequest request = new SearchRequest(INDEX);
        // 条件构造
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        if (StrUtil.isNotBlank(query.getKey())) {
            bool.must(QueryBuilders.matchQuery("name", query.getKey()));
        }
        if (StrUtil.isNotBlank(query.getCategory())) {
            bool.filter(QueryBuilders.termQuery("category", query.getCategory()));
        }
        if (StrUtil.isNotBlank(query.getBrand())) {
            bool.filter(QueryBuilders.termQuery("brand", query.getBrand()));
        }
        if (query.getMinPrice() != null) {
            bool.filter(QueryBuilders.rangeQuery("price").gte(query.getMinPrice()));
        }
        if (query.getMaxPrice() != null) {
            bool.filter(QueryBuilders.rangeQuery("price").lte(query.getMaxPrice()));
        }
        // 排序
        if (StrUtil.isBlank(query.getSortBy())) {
            query.setSortBy("updateTime");
            query.setIsAsc(false);
        }
        request.source()
                .query(bool)
                .sort(query.getSortBy(), query.getIsAsc() ? SortOrder.ASC : SortOrder.DESC);
        // 分页
        request.source()
                .size(query.getPageSize())
                .from((query.getPageNo()-1) * query.getPageSize());
        // 查询
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            List<ItemDoc> items = handleSearchHits(hits.getHits());
            Long total = hits.getTotalHits().value;
            Long pages = (total + query.getPageSize() - 1) / query.getPageSize();
            return PageDTO.fullPage(total, pages, items);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 查询商品包含的所有品牌或分类
    public SearchVO searchByField(ItemPageQuery query) {
        SearchRequest request = new SearchRequest(INDEX);
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        if (StrUtil.isNotBlank(query.getKey())) {
            bool.must(QueryBuilders.matchQuery("name", query.getKey()));
        }else{
            bool.must(QueryBuilders.matchAllQuery());
        }
        if (StrUtil.isNotBlank(query.getCategory())) {
            bool.filter(QueryBuilders.termQuery("category", query.getCategory()));
        }
        if (StrUtil.isNotBlank(query.getBrand())) {
            bool.filter(QueryBuilders.termQuery("brand", query.getBrand()));
        }
        if (query.getMinPrice() != null) {
            bool.filter(QueryBuilders.rangeQuery("price").gte(query.getMinPrice()));
        }
        if (query.getMaxPrice() != null) {
            bool.filter(QueryBuilders.rangeQuery("price").lte(query.getMaxPrice()));
        }
        request.source().query(bool).size(0);
        // 聚合
        request.source()
                .aggregation(AggregationBuilders
                        .terms("brand_agg").field("brand"))
                .aggregation(AggregationBuilders
                        .terms("category_agg").field("category")
                );
        // 查询
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            List<String> brands = getItem(response.getAggregations().get("brand_agg"));
            List<String> categories = getItem(response.getAggregations().get("category_agg"));
            return new SearchVO(categories, brands);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getItem(Terms terms) {
        return terms.getBuckets().stream()
                .map(Terms.Bucket::getKeyAsString)
                .collect(Collectors.toList());
    }

    public List<ItemDoc> handleSearchHits(SearchHit[] hits) {
        return CollUtil.newArrayList(hits).stream()
                .map(hit -> JSONUtil.toBean(hit.getSourceAsString(), ItemDoc.class))
                .collect(Collectors.toList());
    }

    public ItemDoc searchById(String id) {
        GetRequest request = new GetRequest(INDEX).id(id);
        GetResponse response = null;
        try {
            response = client.get(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String json = response.getSourceAsString();
        ItemDoc itemDoc = JSONUtil.toBean(json, ItemDoc.class);
        return itemDoc;
    }

    public void save(ItemDoc itemDoc) {
        try {
            IndexRequest request = new IndexRequest(INDEX)
                    .id(itemDoc.getId())
                    .source(JSONUtil.toJsonStr(itemDoc), XContentType.JSON);
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("Elastic 商品保存失败，id: {}", itemDoc.getId(), e);
        }
    }

    public void delete(String id) {
        try {
            client.delete(new DeleteRequest(INDEX).id(id), RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("Elastic 商品删除失败，id: {}", id, e);
        }
    }

}

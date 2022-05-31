package com.atguigu.gmall.list.service.impl;
import com.atguigu.gmall.model.list.*;
import com.google.common.collect.Lists;

import com.atguigu.gmall.list.dao.GoodsDao;
import com.atguigu.gmall.list.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/29 10:07
 */
@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private ElasticsearchRestTemplate template;

    @Override
    public void saveGoods(Goods goods) {
        goodsDao.save(goods);
    }

    @Override
    public void deleteGoods(Long skuId) {
        goodsDao.deleteById(skuId);
    }

    @Override
    public SearchResponseVo searchGoods(SearchParam searchParam) {
        Query query = buildQueryBySearchParam(searchParam);
        SearchHits<Goods> hits = template.search(query, Goods.class, IndexCoordinates.of("goods"));
        SearchResponseVo responseVo = packageSearchResult(hits,searchParam);
        return responseVo;
    }

    @Override
    public void incrHotScore(Long skuId, Long score) {
        log.info("{} 商品的热度 被更新为 {}",skuId,score);
        //1、先查到sku的信息
        Optional<Goods> byId = goodsDao.findById(skuId);
        Goods goods = byId.get();
        goods.setHotScore(score);

        //2、再保存更新一下
        goodsDao.save(goods);
    }

    private SearchResponseVo packageSearchResult(SearchHits<Goods> hits,SearchParam searchParam) {
        SearchResponseVo responseVo = new SearchResponseVo();
        //封装品牌列表
        responseVo.setTrademarkList(packageTrademarkList(hits));
        //封装属性列表
        responseVo.setAttrsList(packageAttrsList(hits));
//        responseVo.setPropsParamList(Lists.newArrayList());

        List<Goods> goodsList = new ArrayList<>();
        for (SearchHit<Goods> hit : hits) {
            Goods goods = hit.getContent();
            goodsList.add(goods);
        }
        responseVo.setGoodsList(goodsList);
        responseVo.setTotal(hits.getTotalHits());
        responseVo.setPageSize(searchParam.getPageSize());
        responseVo.setPageNo(searchParam.getPageNo());
        Long page = hits.getTotalHits() % searchParam.getPageSize() == 0 ? hits.getTotalHits() / searchParam.getPageSize() : hits.getTotalHits() / searchParam.getPageSize() + 1;
        responseVo.setTotalPages(page);

        return responseVo;
    }

    private List<SearchResponseAttrVo> packageAttrsList(SearchHits<Goods> hits) {
        Aggregations aggregations = hits.getAggregations();
        List<SearchResponseAttrVo> list = new ArrayList<>();
        ParsedNested nestAgg = aggregations.get("nestAgg");
        Aggregations nestAggAggregations = nestAgg.getAggregations();
        ParsedLongTerms attrIdAgg = nestAggAggregations.get("attrIdAgg");
        List<? extends Terms.Bucket> buckets = attrIdAgg.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            SearchResponseAttrVo attrVo = new SearchResponseAttrVo();
            Number attrId = bucket.getKeyAsNumber();
            attrVo.setAttrId(attrId.longValue());
            Aggregations bucketAggregations = bucket.getAggregations();
            ParsedStringTerms attrNameAgg = bucketAggregations.get("attrNameAgg");
            attrVo.setAttrName(attrNameAgg.getBuckets().get(0).getKeyAsString());
            ParsedStringTerms attrValueAgg = bucketAggregations.get("attrValueAgg");
            List<? extends Terms.Bucket> attrValueAggBuckets = attrValueAgg.getBuckets();
            List<String> attrValueList = new ArrayList<>();
            for (Terms.Bucket attrValueAggBucket : attrValueAggBuckets) {
                String attrValue = attrValueAggBucket.getKeyAsString();
                attrValueList.add(attrValue);
            }
            attrVo.setAttrValueList(attrValueList);
            list.add(attrVo);
        }
        return list;
    }

    private List<SearchResponseTmVo> packageTrademarkList(SearchHits<Goods> hits) {
        Aggregations aggregations = hits.getAggregations();
        List<SearchResponseTmVo> list = new ArrayList<>();
        System.out.println(aggregations);
        ParsedLongTerms tmIdAgg = aggregations.get("tmIdAgg");
        List<? extends Terms.Bucket> buckets = tmIdAgg.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            SearchResponseTmVo tmVo = new SearchResponseTmVo();
            Number tmId = bucket.getKeyAsNumber();
            //设置品牌id
            tmVo.setTmId(tmId.longValue());
            //设置品牌名
            Aggregations tmAggs = bucket.getAggregations();
            ParsedStringTerms tmNameAgg = tmAggs.get("tmNameAgg");
            List<? extends Terms.Bucket> tmNameAggBuckets = tmNameAgg.getBuckets();
            tmVo.setTmName(tmNameAggBuckets.get(0).getKeyAsString());
            //设置品牌属性
            ParsedStringTerms tmLogoAgg = tmAggs.get("tmLogoAgg");
            List<? extends Terms.Bucket> tmLogoAggBuckets = tmLogoAgg.getBuckets();
            for (Terms.Bucket tmLogoAggBucket : tmLogoAggBuckets) {
                String tmLogoUrl = tmLogoAggBucket.getKeyAsString();
                tmVo.setTmLogoUrl(tmLogoUrl);
            }
            list.add(tmVo);
        }
        return list;
    }

    private Query buildQueryBySearchParam(SearchParam searchParam) {
        //构造一个完整的查询
        NativeSearchQuery dsl = null;
        //===========构建查询开始=============
        //构造一个bool类型的查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //根据分类查询
        if (searchParam.getCategory1Id() != null){
            boolQuery.must(QueryBuilders.termQuery("category1Id", searchParam.getCategory1Id()));
        }
        if (searchParam.getCategory2Id() != null){
            boolQuery.must(QueryBuilders.termQuery("category2Id", searchParam.getCategory2Id()));
        }
        if (searchParam.getCategory3Id() != null){
            boolQuery.must(QueryBuilders.termQuery("category3Id", searchParam.getCategory3Id()));
        }
        //根据平台属性查询
        String[] props = searchParam.getProps();
        if (props != null && props.length > 0) {
            //props=23:8G:运行内存
            for (String prop : props) {
                String[] split = prop.split(":");
                BoolQueryBuilder bool = QueryBuilders.boolQuery();
                bool.must(QueryBuilders.termQuery("attrs.attrName", split[2]));
                bool.must(QueryBuilders.termQuery("attrs.attrValue", split[1]));

                NestedQueryBuilder attrs = QueryBuilders.nestedQuery("attrs", bool, ScoreMode.None);
                boolQuery.must(attrs);
            }
        }
        //根据品牌查询:trademark=1:小米
        String trademark = searchParam.getTrademark();
        if (!StringUtils.isEmpty(trademark)){
            String[] split = trademark.split(":");
            boolQuery.must(QueryBuilders.termQuery("tmId", split[0]));
        }
        //根据关键字模糊查询
        String keyword = searchParam.getKeyword();
        if (!StringUtils.isEmpty(keyword)){
            boolQuery.must(QueryBuilders.matchQuery("title", keyword));
        }

        //=============构建查询结束============
        dsl = new NativeSearchQuery(boolQuery);
        //=============构建排序开始============
        //order=1:desc/order=2:asc
        String order = searchParam.getOrder();
        if (!StringUtils.isEmpty(order)){
            String[] split = order.split(":");
            String orderField = "1".equals(split[0])?"hotScore":"price";
            Sort sort = Sort.by(Sort.Direction.fromString(split[1]), orderField);
            dsl.addSort(sort);
        }
        //=============构建排序结束============
        //=============构建分页开始============
        Pageable pageable = PageRequest.of(searchParam.getPageNo() - 1, searchParam.getPageSize());
        dsl.setPageable(pageable);
        //=============构建分页结束============

        //=============构建高亮开始============
        if (!StringUtils.isEmpty(keyword)){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title").preTags("<span style='color:red'>").postTags("</span>");
            HighlightQuery highlightQuery = new HighlightQuery(highlightBuilder);
            dsl.setHighlightQuery(highlightQuery);
        }
        //=============构建高亮结束============


        //============品牌列表聚合开始==========
        TermsAggregationBuilder tmIdAgg = AggregationBuilders.terms("tmIdAgg").field("tmId").size(100);
        tmIdAgg.subAggregation(AggregationBuilders.terms("tmNameAgg").field("tmName").size(1));
        tmIdAgg.subAggregation(AggregationBuilders.terms("tmLogoAgg").field("tmLogoUrl").size(100));

        dsl.addAggregation(tmIdAgg);
        //============品牌列表聚合结束==========

        //=========平台属性列表聚合开始=========
        NestedAggregationBuilder nestAgg = AggregationBuilders.nested("nestAgg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId").size(100);
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(1));
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue").size(100));
        nestAgg.subAggregation(attrIdAgg);

        dsl.addAggregation(nestAgg);
        //=========平台属性列表聚合结束=========
        return dsl;
    }
}

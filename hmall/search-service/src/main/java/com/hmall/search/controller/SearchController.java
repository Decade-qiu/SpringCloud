package com.hmall.search.controller;

import cn.hutool.json.JSONUtil;
import com.hmall.api.dto.ItemDTO;
import com.hmall.search.domain.DTO.PageDTO;
import com.hmall.search.domain.po.ItemDoc;
import com.hmall.search.domain.query.ItemPageQuery;
import com.hmall.search.domain.vo.SearchVO;
import com.hmall.search.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(tags = "搜索相关接口")
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @ApiOperation("根据ID搜索商品")
    @GetMapping("/{id}")
    public ItemDoc search(@PathVariable String id) {
        return searchService.searchById(id);
    }

    @ApiOperation("搜索商品")
    @GetMapping("/list")
    public PageDTO<ItemDoc> search(ItemPageQuery query) {
        return searchService.search(query);
    }

    @ApiOperation("搜索商品")
    @PostMapping("/filters")
    public SearchVO searchByField(@RequestBody ItemPageQuery query) {
        return searchService.searchByField(query);
    }

    // @ApiOperation("保存商品")
    // @PostMapping
    // public void save(@RequestBody ItemDoc itemDoc) {
    //     searchService.save(itemDoc);
    // }

    // @ApiOperation("搜索商品")
    // @GetMapping("/list")
    // public PageDTO<ItemDTO> search(ItemPageQuery query) {
    //     // 分页查询
    //     Page<Item> result = query.toMpPage("update_time", false);
    //     itemService.lambdaQuery()
    //             .like(StrUtil.isNotBlank(query.getKey()), Item::getName, query.getKey())
    //             .eq(StrUtil.isNotBlank(query.getBrand()), Item::getBrand, query.getBrand())
    //             .eq(StrUtil.isNotBlank(query.getCategory()), Item::getCategory, query.getCategory())
    //             .eq(Item::getStatus, 1)
    //             .between(query.getMaxPrice() != null, Item::getPrice, query.getMinPrice(), query.getMaxPrice())
    //             .page(result);
    //     // 封装并返回
    //     return PageDTO.of(result, ItemDTO.class);
    // }

}

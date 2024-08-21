package com.hmall.api.client;

import com.hmall.api.dto.ItemDTO;
import com.hmall.api.dto.OrderDetailDTO;
import com.hmall.common.utils.BeanUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("item-service")
public interface ItemClient {

    @GetMapping("/items/{id}")
    ItemDTO queryItemById(@PathVariable("id") Long id);

    @GetMapping("/items")
    List<ItemDTO> queryItems(@RequestParam("ids") List<Long> ids);

    @PutMapping("/items/stock/deduct")
    void deductStock(@RequestBody List<OrderDetailDTO> items);

    @PutMapping("/items/stock/restore")
    void restoreStock(@RequestBody List<OrderDetailDTO> items);

}

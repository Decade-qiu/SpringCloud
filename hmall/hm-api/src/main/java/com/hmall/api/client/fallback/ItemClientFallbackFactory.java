package com.hmall.api.client.fallback;

import com.hmall.api.client.ItemClient;
import com.hmall.api.dto.ItemDTO;
import com.hmall.api.dto.OrderDetailDTO;
import com.hmall.common.utils.CollUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

@Slf4j
public class ItemClientFallbackFactory implements FallbackFactory<ItemClient> {

    @Override
    public ItemClient create(Throwable cause) {
        return new ItemClient() {
            @Override
            public ItemDTO queryItemById(Long id) {
                log.error("Failed to query item, id: {}", id, cause);
                throw new RuntimeException("Failed to query item");
            }

            @Override
            public List<ItemDTO> queryItems(List<Long> ids) {
                log.error("Failed to query items, ids: {}", ids, cause);
                return CollUtils.emptyList();
            }

            @Override
            public void deductStock(List<OrderDetailDTO> items) {
                log.error("Failed to deduct stock, items: {}", items, cause);
                throw new RuntimeException("Failed to deduct stock");
            }

            @Override
            public void restoreStock(List<OrderDetailDTO> items) {
                log.error("Failed to restore stock, items: {}", items, cause);
                throw new RuntimeException("Failed to restore stock");
            }
        };
    }
}

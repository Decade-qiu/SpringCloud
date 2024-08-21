package com.hmall.search.listener;

import com.hmall.api.client.ItemClient;
import com.hmall.api.dto.ItemDTO;
import com.hmall.common.utils.BeanUtils;
import com.hmall.search.domain.po.ItemDoc;
import com.hmall.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemListener {

    private final SearchService searchService;

    private final ItemClient itemClient;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    value = "item.update.queue",
                    durable = "true"
            ),
            exchange = @Exchange(
                    value = "search.direct"
            ),
            key = {"item.update"}
    ))
    public void listenItemUpdate(Long id) {
        ItemDTO itemDTO = itemClient.queryItemById(id);
        ItemDoc itemDoc = BeanUtils.copyBean(itemDTO, ItemDoc.class);
        searchService.save(itemDoc);
    }
    // public void listenItemUpdate(Message<Long> message) {
    //     // 获取消息体
    //     Long id = message.getPayload();
    //     System.out.println("Received message: " + id);
    //     // 如果需要，可以获取消息头信息
    //     MessageHeaders headers = message.getHeaders();
    //     System.out.println("Received message with headers: " + headers);
    // }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    value = "item.add.queue",
                    durable = "true"
            ),
            exchange = @Exchange(
                    value = "search.direct"
            ),
            key = {"item.add"}
    ))
    public void listenItemAdd(Long id) {
        ItemDTO itemDTO = itemClient.queryItemById(id);
        ItemDoc itemDoc = BeanUtils.copyBean(itemDTO, ItemDoc.class);
        if (itemDTO.getStatus() == 1){
            searchService.save(itemDoc);
        }else {
            searchService.delete(String.valueOf(id));
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    value = "item.delete.queue",
                    durable = "true"
            ),
            exchange = @Exchange(
                    value = "search.direct"
            ),
            key = {"item.delete"}
    ))
    public void listenItemDelete(Long id) {
        searchService.delete(String.valueOf(id));
    }

}

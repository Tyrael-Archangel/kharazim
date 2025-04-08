package com.tyrael.kharazim.pharmacy.app.mq;

import com.tyrael.kharazim.mq.Message;
import com.tyrael.kharazim.pharmacy.sdk.model.message.CreatePurchaseInboundOrderMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/4/7
 */
@SpringBootTest
public class CreatePurchaseInboundOrderListenerTest {

    @Autowired
    private CreatePurchaseInboundOrderListener listener;

    @Test
    public void createInboundOrder() {

        Message<CreatePurchaseInboundOrderMessage> message = new Message<>();
        message.setId("fc82e41d5e1f4e8db15cfb9d6636175b");
        message.setTopic("CREATE_PURCHASE_INBOUND_ORDER");
        message.setTimestamp(1743995767455L);
        message.setBody(
                new CreatePurchaseInboundOrderMessage()
                        .setClinicCode("CL0011")
                        .setSupplierCode("SU0001")
                        .setSourceRemark("测试手动创建")
                        .setSourceBusinessCode("PO20250407001")
                        .setItems(List.of(
                                new CreatePurchaseInboundOrderMessage.Item()
                                        .setSkuCode("P250319000001")
                                        .setQuantity(6)
                        ))
        );

        listener.consume(message);
    }

}

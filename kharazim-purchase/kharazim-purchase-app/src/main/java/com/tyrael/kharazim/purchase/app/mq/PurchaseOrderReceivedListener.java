package com.tyrael.kharazim.purchase.app.mq;

import com.tyrael.kharazim.mq.MqConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2025/4/3
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PurchaseOrderReceivedListener implements MqConsumer<String> {

    @Override
    public String getTopic() {
        return "PURCHASE_ORDER_RECEIVED";
    }

    @Override
    public void consume(String body) {
        log.info("Received purchase order received message: {}", body);
    }

}

package com.tyrael.kharazim.pharmacy.app.mq;


import com.tyrael.kharazim.mq.MqConsumer;
import com.tyrael.kharazim.pharmacy.app.service.InboundOrderService;
import com.tyrael.kharazim.pharmacy.model.message.CreatePurchaseInboundOrderMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2025/4/2
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CreatePurchaseInboundOrderListener implements MqConsumer<CreatePurchaseInboundOrderMessage> {

    private final InboundOrderService inboundOrderService;

    @Override
    public String getTopic() {
        return "CREATE_PURCHASE_INBOUND_ORDER";
    }

    @Override
    public void consume(CreatePurchaseInboundOrderMessage body) {
        log.info("Received CreateInboundOrderMessage: {}", body);
        inboundOrderService.createFromPurchase(body);
    }

}

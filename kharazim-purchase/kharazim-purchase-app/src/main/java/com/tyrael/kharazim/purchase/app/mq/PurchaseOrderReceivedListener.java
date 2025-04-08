package com.tyrael.kharazim.purchase.app.mq;

import com.tyrael.kharazim.mq.MqConsumer;
import com.tyrael.kharazim.pharmacy.sdk.model.message.InboundOrderReceivedMessage;
import com.tyrael.kharazim.purchase.app.service.PurchaseOrderReceivedService;
import com.tyrael.kharazim.purchase.app.vo.purchaseorder.PurchaseOrderReceivedVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/4/3
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PurchaseOrderReceivedListener implements MqConsumer<InboundOrderReceivedMessage> {

    private final PurchaseOrderReceivedService purchaseOrderReceivedService;

    @Override
    public String getTopic() {
        return "INBOUND_ORDER_RECEIVED";
    }

    @Override
    public void consume(InboundOrderReceivedMessage body) {
        log.info("Received purchase order received message: {}", body);
        List<PurchaseOrderReceivedVO.Item> items = body.getItems()
                .stream()
                .map(e -> new PurchaseOrderReceivedVO.Item(e.getSkuCode(), e.getQuantity()))
                .toList();
        PurchaseOrderReceivedVO receivedVO = new PurchaseOrderReceivedVO(
                body.getSourceBusinessCode(),
                body.getSerialCode(),
                items,
                body.getOperator(),
                body.getOperatorCode());
        purchaseOrderReceivedService.receive(receivedVO);
    }

}

package com.tyrael.kharazim.purchase.app.event;

import com.tyrael.kharazim.mq.MqProducer;
import com.tyrael.kharazim.pharmacy.sdk.model.message.CreatePurchaseInboundOrderMessage;
import com.tyrael.kharazim.purchase.app.domain.PurchaseOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/3/31
 */
@Component
@RequiredArgsConstructor
public class PurchaseOrderCreatedEventListener {

    private final MqProducer mqProducer;

    @EventListener
    public void onPurchaseOrderCreated(PurchaseOrderCreatedEvent event) {

        PurchaseOrder purchaseOrder = event.getPurchaseOrder();

        CreatePurchaseInboundOrderMessage message = new CreatePurchaseInboundOrderMessage();
        message.setSourceBusinessCode(purchaseOrder.getCode());
        message.setClinicCode(purchaseOrder.getClinicCode());
        message.setSupplierCode(purchaseOrder.getSupplierCode());
        message.setSourceRemark(purchaseOrder.getRemark());

        List<CreatePurchaseInboundOrderMessage.Item> items = purchaseOrder.getItems()
                .stream()
                .map(e -> new CreatePurchaseInboundOrderMessage.Item()
                        .setSkuCode(e.getSkuCode())
                        .setQuantity(e.getQuantity()))
                .collect(Collectors.toList());

        message.setItems(items);

        mqProducer.send("CREATE_PURCHASE_INBOUND_ORDER", message);

    }

}

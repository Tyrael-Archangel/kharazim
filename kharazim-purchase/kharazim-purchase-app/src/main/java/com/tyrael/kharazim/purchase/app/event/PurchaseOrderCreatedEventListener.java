package com.tyrael.kharazim.purchase.app.event;

import com.tyrael.kharazim.purchase.app.domain.PurchaseOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2025/3/31
 */
@Component
@RequiredArgsConstructor
public class PurchaseOrderCreatedEventListener {

    @EventListener
    public void onPurchaseOrderCreated(PurchaseOrderCreatedEvent event) {
        PurchaseOrder purchaseOrder = event.getPurchaseOrder();
    }

}

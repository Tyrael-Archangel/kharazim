package com.tyrael.kharazim.purchase.app.event;

import com.tyrael.kharazim.purchase.app.domain.PurchaseOrder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Tyrael Archangel
 * @since 2025/3/31
 */
@Getter
public class PurchaseOrderCreatedEvent extends ApplicationEvent {

    private final PurchaseOrder purchaseOrder;

    public PurchaseOrderCreatedEvent(Object source, PurchaseOrder purchaseOrder) {
        super(source);
        this.purchaseOrder = purchaseOrder;
    }

}

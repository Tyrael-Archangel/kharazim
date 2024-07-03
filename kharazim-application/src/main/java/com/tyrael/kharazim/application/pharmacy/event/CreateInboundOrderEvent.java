package com.tyrael.kharazim.application.pharmacy.event;

import com.tyrael.kharazim.application.purchase.domain.PurchaseOrder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Tyrael Archangel
 * @since 2024/7/3
 */
@Getter
public class CreateInboundOrderEvent extends ApplicationEvent {

    private final PurchaseOrder purchaseOrder;

    public CreateInboundOrderEvent(Object source, PurchaseOrder purchaseOrder) {
        super(source);
        this.purchaseOrder = purchaseOrder;
    }
}

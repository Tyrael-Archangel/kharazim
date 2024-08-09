package com.tyrael.kharazim.application.purchase.event;

import com.tyrael.kharazim.application.purchase.vo.PurchaseOrderReceivedVO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Tyrael Archangel
 * @since 2024/8/9
 */
@Getter
public class PurchaseOrderReceivedEvent extends ApplicationEvent {

    private final PurchaseOrderReceivedVO purchaseOrderReceivedVO;

    public PurchaseOrderReceivedEvent(Object source, PurchaseOrderReceivedVO purchaseOrderReceivedVO) {
        super(source);
        this.purchaseOrderReceivedVO = purchaseOrderReceivedVO;
    }

}

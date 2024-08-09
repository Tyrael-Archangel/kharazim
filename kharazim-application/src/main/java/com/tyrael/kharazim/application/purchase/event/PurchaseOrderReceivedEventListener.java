package com.tyrael.kharazim.application.purchase.event;

import com.tyrael.kharazim.application.purchase.service.PurchaseOrderReceivedService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2024/8/9
 */
@Component
@RequiredArgsConstructor
public class PurchaseOrderReceivedEventListener {

    private final PurchaseOrderReceivedService purchaseOrderReceivedService;

    @EventListener
    public void listen(PurchaseOrderReceivedEvent event) {
        purchaseOrderReceivedService.receive(event.getPurchaseOrderReceivedVO());
    }

}

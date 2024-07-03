package com.tyrael.kharazim.application.pharmacy.event;

import com.tyrael.kharazim.application.pharmacy.service.InboundOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2024/7/3
 */
@Component
@RequiredArgsConstructor
public class CreateInboundOrderEventListener {

    private final InboundOrderService inboundOrderService;

    @EventListener
    public void listen(CreateInboundOrderEvent event) {
        inboundOrderService.createFromPurchaseOrder(event.getPurchaseOrder());
    }

}

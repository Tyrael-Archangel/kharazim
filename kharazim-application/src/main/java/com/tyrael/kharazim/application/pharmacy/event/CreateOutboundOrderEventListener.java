package com.tyrael.kharazim.application.pharmacy.event;

import com.tyrael.kharazim.application.pharmacy.service.OutboundOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@Component
@RequiredArgsConstructor
public class CreateOutboundOrderEventListener {

    private final OutboundOrderService outboundOrderService;

    @EventListener
    public void listen(CreateOutboundOrderEvent event) {
        outboundOrderService.createFromPrescription(event.getPrescription());
    }

}

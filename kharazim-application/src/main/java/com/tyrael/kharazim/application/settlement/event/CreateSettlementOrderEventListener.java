package com.tyrael.kharazim.application.settlement.event;

import com.tyrael.kharazim.application.settlement.service.SettlementOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Component
@RequiredArgsConstructor
public class CreateSettlementOrderEventListener {

    private final SettlementOrderService settlementOrderService;

    @EventListener
    public void listen(CreateSettlementOrderEvent event) {
        settlementOrderService.createFromPrescription(event.getPrescription());
    }

}

package com.tyrael.kharazim.finance.app.mq;

import com.tyrael.kharazim.finance.app.service.SettlementOrderService;
import com.tyrael.kharazim.finance.sdk.model.message.CreatePrescriptionSettlementMessage;
import com.tyrael.kharazim.mq.MqConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2025/4/8
 */
@Component
@RequiredArgsConstructor
public class CreatePrescriptionSettlementListener implements MqConsumer<CreatePrescriptionSettlementMessage> {

    private final SettlementOrderService settlementOrderService;

    @Override
    public String getTopic() {
        return "CREATE_PRESCRIPTION_SETTLEMENT";
    }

    @Override
    public void consume(CreatePrescriptionSettlementMessage body) {
        settlementOrderService.createFromPrescription(body);
    }

}

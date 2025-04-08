package com.tyrael.kharazim.diagnosistreatment.app.mq;

import com.tyrael.kharazim.diagnosistreatment.app.service.prescription.PrescriptionLifecycleService;
import com.tyrael.kharazim.finance.sdk.model.message.SettlementOrderPaidMessage;
import com.tyrael.kharazim.mq.MqConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2025/4/8
 */
@Component
@RequiredArgsConstructor
public class PrescriptionPaidCallbackListener implements MqConsumer<SettlementOrderPaidMessage> {

    private final PrescriptionLifecycleService prescriptionLifecycleService;

    @Override
    public String getTopic() {
        return "SETTLEMENT_ORDER_PAID";
    }

    @Override
    public void consume(SettlementOrderPaidMessage body) {
        String sourcePrescriptionCode = body.getSourcePrescriptionCode();
        LocalDateTime paidTime = body.getPaidTime();
        prescriptionLifecycleService.paidCallback(sourcePrescriptionCode, paidTime);
    }

}

package com.tyrael.kharazim.diagnosistreatment.app.mq;

import com.tyrael.kharazim.diagnosistreatment.app.service.prescription.PrescriptionLifecycleService;
import com.tyrael.kharazim.mq.MqConsumer;
import com.tyrael.kharazim.pharmacy.sdk.model.message.InventoryOccupyResultMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2025/4/8
 */
@Component
@RequiredArgsConstructor
public class PrescriptionInventoryOccupyCallbackListener implements MqConsumer<InventoryOccupyResultMessage> {

    private final PrescriptionLifecycleService prescriptionLifecycleService;

    @Override
    public String getTopic() {
        return "INVENTORY_OCCUPY_RESULT";
    }

    @Override
    public void consume(InventoryOccupyResultMessage body) {
        boolean occupied = body.occupySuccessful();
        String prescriptionCode = body.getOccupyMessage().getBusinessCode();
        prescriptionLifecycleService.occupyCallback(prescriptionCode, occupied);
    }

}

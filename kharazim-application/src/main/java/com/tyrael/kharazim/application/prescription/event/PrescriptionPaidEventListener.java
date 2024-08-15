package com.tyrael.kharazim.application.prescription.event;

import com.tyrael.kharazim.application.prescription.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@Component
@RequiredArgsConstructor
public class PrescriptionPaidEventListener {

    private final PrescriptionService prescriptionService;

    @EventListener
    public void listen(PrescriptionPaidEvent event) {
        prescriptionService.paidCallback(event.getPrescriptionCode());
    }

}

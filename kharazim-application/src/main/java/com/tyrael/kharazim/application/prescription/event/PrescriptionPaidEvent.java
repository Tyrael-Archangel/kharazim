package com.tyrael.kharazim.application.prescription.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@Getter
public class PrescriptionPaidEvent extends ApplicationEvent {

    private final String prescriptionCode;

    public PrescriptionPaidEvent(Object source, String prescriptionCode) {
        super(source);
        this.prescriptionCode = prescriptionCode;
    }
}

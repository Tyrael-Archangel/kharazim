package com.tyrael.kharazim.application.pharmacy.event;

import com.tyrael.kharazim.application.prescription.domain.Prescription;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@Getter
public class CreateOutboundOrderEvent extends ApplicationEvent {

    private final Prescription prescription;

    public CreateOutboundOrderEvent(Object source, Prescription prescription) {
        super(source);
        this.prescription = prescription;
    }

}

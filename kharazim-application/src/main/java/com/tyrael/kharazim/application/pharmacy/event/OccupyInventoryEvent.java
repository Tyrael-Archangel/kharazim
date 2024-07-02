package com.tyrael.kharazim.application.pharmacy.event;

import com.tyrael.kharazim.application.prescription.domain.Prescription;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Tyrael Archangel
 * @since 2024/7/2
 */
@Getter
public class OccupyInventoryEvent extends ApplicationEvent {

    /**
     * 处方
     */
    private final Prescription prescription;

    public OccupyInventoryEvent(Object source, Prescription prescription) {
        super(source);
        this.prescription = prescription;
    }
}

package com.tyrael.kharazim.application.settlement.event;

import com.tyrael.kharazim.application.prescription.domain.Prescription;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Getter
public class CreateSettlementOrderEvent extends ApplicationEvent {

    /**
     * 处方
     */
    private final Prescription prescription;

    public CreateSettlementOrderEvent(Object source, Prescription prescription) {
        super(source);
        this.prescription = prescription;
    }

}

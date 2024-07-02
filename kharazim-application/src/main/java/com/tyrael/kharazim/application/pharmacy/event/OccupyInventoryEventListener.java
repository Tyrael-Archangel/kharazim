package com.tyrael.kharazim.application.pharmacy.event;

import com.tyrael.kharazim.application.pharmacy.service.InventoryOccupyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2024/7/2
 */
@Component
@RequiredArgsConstructor
public class OccupyInventoryEventListener {

    private final InventoryOccupyService inventoryOccupyService;

    @EventListener
    public void listen(OccupyInventoryEvent event) {
        inventoryOccupyService.occupyByPrescription(event.getPrescription());
    }

}

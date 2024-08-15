package com.tyrael.kharazim.application.pharmacy.event;

import com.tyrael.kharazim.application.pharmacy.service.InventoryService;
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

    private final InventoryService inventoryService;

    @EventListener
    public void listen(OccupyInventoryEvent event) {
        inventoryService.occupyByPrescription(event.getPrescription());
    }

}

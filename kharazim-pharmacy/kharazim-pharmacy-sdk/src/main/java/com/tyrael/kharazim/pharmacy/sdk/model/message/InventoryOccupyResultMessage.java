package com.tyrael.kharazim.pharmacy.sdk.model.message;

import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2025/4/7
 */
@Data
public class InventoryOccupyResultMessage {

    private InventoryOccupyMessage occupyMessage;
    private Boolean success;

    public boolean occupySuccessful() {
        return Boolean.TRUE.equals(success);
    }

}

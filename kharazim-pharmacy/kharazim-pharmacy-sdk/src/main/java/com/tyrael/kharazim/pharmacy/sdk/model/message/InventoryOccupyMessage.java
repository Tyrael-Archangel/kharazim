package com.tyrael.kharazim.pharmacy.sdk.model.message;

import lombok.Data;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/4/7
 */
@Data
public class InventoryOccupyMessage {

    private String businessCode;
    private String clinicCode;
    private List<Item> items;
    private String operator;
    private String operatorCode;

    @Data
    public static class Item {
        private String skuCode;
        private Integer quantity;
    }

}

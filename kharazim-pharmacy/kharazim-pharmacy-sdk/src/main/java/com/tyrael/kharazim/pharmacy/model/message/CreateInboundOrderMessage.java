package com.tyrael.kharazim.pharmacy.model.message;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/4/2
 */
@Data
public class CreateInboundOrderMessage {

    private String purchaseOrderNumber;
    private LocalDateTime createdTime;
    private List<Item> items;

    @Data
    public static class Item {
        private String skuCode;
        private Integer quantity;
    }

}

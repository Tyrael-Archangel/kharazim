package com.tyrael.kharazim.finance.sdk.model.message;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/4/8
 */
@Data
public class CreatePrescriptionSettlementMessage {

    private String prescriptionCode;
    private String customerCode;
    private String clinicCode;
    private BigDecimal totalAmount;
    private List<Item> items;

    @Data
    public static class Item {
        private String skuCode;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal amount;
    }

}

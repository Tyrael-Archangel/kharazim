package com.tyrael.kharazim.pharmacy.model.message;

import lombok.Data;

import java.util.List;

/**
 * 入库单已收货消息
 *
 * @author Tyrael Archangel
 * @since 2025/4/7
 */
@Data
public class InboundOrderReceivedMessage {

    private String sourceBusinessCode;
    private String serialCode;
    private List<Item> items;
    private String operator;
    private String operatorCode;

    @Data
    public static class Item {
        private String skuCode;
        private Integer quantity;
    }
}

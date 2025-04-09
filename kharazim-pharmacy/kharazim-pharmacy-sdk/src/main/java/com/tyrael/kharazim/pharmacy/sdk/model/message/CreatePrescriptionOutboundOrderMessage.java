package com.tyrael.kharazim.pharmacy.sdk.model.message;

import lombok.Data;

import java.util.List;

/**
 * 处方出库单消息体
 *
 * @author Tyrael Archangel
 * @since 2025/4/2
 */
@Data
public class CreatePrescriptionOutboundOrderMessage {

    /**
     * 来源单据编码
     */
    private String sourceBusinessCode;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 诊所编码
     */
    private String clinicCode;

    /**
     * 来源备注
     */
    private String remark;

    /**
     * 商品信息
     */
    private List<Item> items;

    @Data
    public static class Item {
        /**
         * sku
         */
        private String skuCode;
        /**
         * 数量
         */
        private Integer quantity;
    }

}

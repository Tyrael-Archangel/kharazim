package com.tyrael.kharazim.pharmacy.model.message;

import lombok.Data;

import java.util.List;

/**
 * 采购入库单消息体
 *
 * @author Tyrael Archangel
 * @since 2025/4/2
 */
@Data
public class CreatePurchaseInboundOrderMessage {

    /**
     * 来源单据编码
     */
    private String sourceBusinessCode;

    /**
     * 诊所编码
     */
    private String clinicCode;

    /**
     * 供应商编码
     */
    private String supplierCode;

    /**
     * 来源备注
     */
    private String sourceRemark;

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

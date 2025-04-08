package com.tyrael.kharazim.pharmacy.sdk.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Tyrael Archangel
 * @since 2025/4/8
 */
@Data
public class InventoryVO implements Serializable {

    /**
     * 诊所编码
     */
    private String clinicCode;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * 在库库存数量
     */
    private Integer quantity;

    /**
     * 已预占数量
     */
    private Integer occupiedQuantity;

    /**
     * 可用库存数量
     */
    private Integer usableQuantity;

}

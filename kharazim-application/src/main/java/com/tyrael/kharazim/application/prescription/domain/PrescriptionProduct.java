package com.tyrael.kharazim.application.prescription.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 处方商品
 *
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
@Data
public class PrescriptionProduct {

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单价（元）
     */
    private BigDecimal price;

}

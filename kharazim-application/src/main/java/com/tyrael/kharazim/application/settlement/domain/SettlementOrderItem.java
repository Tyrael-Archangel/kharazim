package com.tyrael.kharazim.application.settlement.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Data
public class SettlementOrderItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 结算单编码
     */
    private String settlementOrderCode;

    /**
     * SKU编码
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

    /**
     * 商品项金额（元）
     */
    private BigDecimal amount;

}

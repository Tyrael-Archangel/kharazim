package com.tyrael.kharazim.pos.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.math.BigDecimal;

/**
 * 单个数量商品明细
 *
 * @author Tyrael Archangel
 * @since 2025/7/17
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClinicOrderLineItemFlat {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long orderLineItemId;

    /**
     * 商品折后单价 = {@linkplain #salePrice 商品原价} - {@linkplain #productDiscount 产品折扣} - {@linkplain #allocatedOrderDiscount 分摊的订单折扣}
     */
    private BigDecimal price;

    /**
     * {@linkplain ClinicOrderLineItem#getSalePrice 商品原价}
     */
    private BigDecimal salePrice;

    /**
     * 产品折扣（直接作用于商品上的折扣）
     */
    private BigDecimal productDiscount;

    /**
     * 分摊的订单折扣（订单维度的折扣分摊到单个商品上的折扣）
     */
    private BigDecimal allocatedOrderDiscount;

    /**
     * 是否已经售后
     */
    private Boolean refunded;

}

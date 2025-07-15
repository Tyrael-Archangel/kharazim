package com.tyrael.kharazim.pos.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2025/7/15
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShopOrderLineItem {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 该item项的修订版本号，初始值0，每次修改+1
     */
    private Integer revise;

    private Long orderId;

    private String skuCode;
    private String spuCode;
    private String upcCode;
    private String taxCode;
    private String productName;
    private String imageUrl;
    private String attributes;
    private Long seriesId;
    private String seriesName;

    /**
     * 数量
     */
    private Integer quantity;
    private Integer currentQuantity;

    /**
     * 商品原价
     */
    private BigDecimal salePrice;

    /**
     * 该item项所有数量的折扣金额合计（产品折扣+订单折扣）
     */
    private BigDecimal discountPrice;
    private BigDecimal currentDiscountPrice;

    /**
     * 该item项所有数量的税费金额合计
     */
    private BigDecimal taxPrice;
    private BigDecimal currentTaxPrice;

}

package com.tyrael.kharazim.pos.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2025/7/17
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShopOrderTaxLineItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long orderLineItemId;
    private Integer orderLineItemRevise;

    private String title;

    private String taxCode;

    private BigDecimal taxRate;

    private BigDecimal taxPrice;

}

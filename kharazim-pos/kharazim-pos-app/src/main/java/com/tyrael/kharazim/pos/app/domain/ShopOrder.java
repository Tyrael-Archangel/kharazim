package com.tyrael.kharazim.pos.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.mybatis.BaseDO;
import com.tyrael.kharazim.pos.app.enums.ShopOrderSourceEnum;
import com.tyrael.kharazim.pos.app.enums.ShopOrderStatusEnum;
import lombok.*;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 店铺订单
 *
 * @author yangjie
 * @since 2025/5/12
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShopOrder extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 经销商
     */
    private String tenantCode;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 店铺订单号
     */
    private String orderNumber;

    @Getter(onMethod_ = {@Nullable})
    private Long customerId;

    /**
     * 订单币种
     */
    private String currency;

    /**
     * 订单状态
     */
    private ShopOrderStatusEnum status;

    private ShopOrderSourceEnum orderSource;

    /**
     * 订单交易额 = {@linkplain #totalLineItemPrice 商品销售价金额合计} + {@linkplain #totalTax 订单总税费} - {@linkplain #totalDiscountPrice 总折扣金额}
     */
    private BigDecimal totalPrice;
    private BigDecimal currentTotalPrice;

    /**
     * 商品销售价金额合计（不含任何折扣、税费）
     */
    private BigDecimal totalLineItemPrice;
    private BigDecimal currentTotalLineItemPrice;

    /**
     * 小计（含折扣、不含税）
     */
    private BigDecimal subtotalPrice;
    private BigDecimal currentSubtotalPrice;

    /**
     * 订单总税费
     */
    private BigDecimal totalTax;
    private BigDecimal currentTotalTax;

    /**
     * 总折扣金额
     * <pre>
     *  总折扣金额 = 商品折扣 + 订单折扣
     * </pre>
     */
    private BigDecimal totalDiscountPrice;
    private BigDecimal currentTotalDiscountPrice;

    private Integer skuCount;
    private Integer currentSkuCount;

    /**
     * 备注信息
     */
    private String note;

    /**
     * 销售员工ID
     */
    private Long saleUserId;
    /**
     * 销售员工名
     */
    private String saleUserName;

    /**
     * 支付渠道
     */
    private String paymentChannel;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 付款成功时间
     */
    private LocalDateTime paidAt;

    /**
     * 销售员工ID
     */
    @Setter
    private Long createdUserId;
    /**
     * 销售员工
     */
    @Setter
    private String createdUserName;
    @Setter
    private LocalDateTime createdAt;

    @Setter
    private Long updatedUserId;
    @Setter
    private String updatedUserName;
    @Setter
    private LocalDateTime updatedAt;

}

package com.tyrael.kharazim.pos.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.mybatis.BaseDO;
import com.tyrael.kharazim.pos.app.enums.ClinicOrderStatusEnum;
import lombok.*;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 店铺订单
 *
 * @author Tyrael Archangel
 * @since 2025/7/15
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClinicOrder extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 店铺ID
     */
    private String clinicCode;

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
    private ClinicOrderStatusEnum status;

    /**
     * 订单交易额 = {@linkplain #totalLineItemAmount 商品销售价金额合计}
     * + {@linkplain #totalTaxAmount 订单总税费}
     * - {@linkplain #orderLevelDiscountAmount 订单折扣}
     * - {@linkplain #totalProductDiscountAmount 商品折扣}
     */
    private BigDecimal totalAmount;
    private BigDecimal currentTotalAmount;

    /**
     * 商品销售价金额合计（不含任何折扣、税费）
     */
    private BigDecimal totalLineItemAmount;
    private BigDecimal currentTotalLineItemAmount;

    /**
     * 小计（含折扣、不含税）
     */
    private BigDecimal subtotalAmount;
    private BigDecimal currentSubtotalAmount;

    /**
     * 订单总税费
     */
    private BigDecimal totalTaxAmount;
    private BigDecimal currentTotalTaxAmount;

    /**
     * 订单折扣
     */
    private BigDecimal orderLevelDiscountAmount;
    private BigDecimal currentOrderLevelDiscountAmount;

    /**
     * 商品折扣
     */
    private BigDecimal totalProductDiscountAmount;
    private BigDecimal currentTotalProductDiscountAmount;

    private Integer skuCount;
    private Integer currentSkuCount;

    /**
     * 备注信息
     */
    private String note;

    /**
     * 销售员工
     */
    private String saleUserCode;

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

}

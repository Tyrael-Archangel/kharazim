package com.tyrael.kharazim.application.purchase.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/5/31
 */
@Data
public class PurchaseOrderPaymentRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 流水号
     */
    private String serialCode;

    /**
     * 采购单号
     */
    private String purchaseOrderCode;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 支付用户
     */
    private String paymentUser;

    /**
     * 支付用户编码
     */
    private String paymentUserCode;

    /**
     * 支付凭证
     */
    private List<String> vouchers;

}

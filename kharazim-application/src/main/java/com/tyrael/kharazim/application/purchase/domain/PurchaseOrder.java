package com.tyrael.kharazim.application.purchase.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.application.base.BaseDO;
import com.tyrael.kharazim.application.purchase.enums.PurchaseOrderPaymentStatus;
import com.tyrael.kharazim.application.purchase.enums.PurchaseOrderReceiveStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 采购单
 *
 * @author Tyrael Archangel
 * @since 2024/5/31
 */
@Data
public class PurchaseOrder extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 采购单号
     */
    private String code;

    /**
     * 诊所编码
     */
    private String clinicCode;

    /**
     * 供应商编码
     */
    private String supplierCode;

    /**
     * 收货状态
     */
    private PurchaseOrderReceiveStatus receiveStatus;

    /**
     * 结算状态
     */
    private PurchaseOrderPaymentStatus paymentStatus;

    /**
     * 总金额（元）
     */
    private BigDecimal totalAmount;

    /**
     * 已结算金额（元）
     */
    private BigDecimal paidAmount;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private List<PurchaseOrderItem> items;

}

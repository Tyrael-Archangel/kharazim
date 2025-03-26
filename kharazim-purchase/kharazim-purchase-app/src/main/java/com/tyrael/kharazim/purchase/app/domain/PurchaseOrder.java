package com.tyrael.kharazim.purchase.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.mybatis.BaseDO;
import com.tyrael.kharazim.purchase.app.enums.PurchasePaymentStatus;
import com.tyrael.kharazim.purchase.app.enums.PurchaseReceiveStatus;
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
    private PurchaseReceiveStatus receiveStatus;

    /**
     * 结算状态
     */
    private PurchasePaymentStatus paymentStatus;

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

    public void refreshReceiveStatus() {
        boolean noneReceive = items.stream()
                .allMatch(e -> e.getReceivedQuantity() == 0);
        if (noneReceive) {
            this.receiveStatus = PurchaseReceiveStatus.WAIT_RECEIVE;
        } else {
            boolean hasAnyNotFinish = items.stream()
                    .anyMatch(e -> e.getRemainQuantity() > 0);
            this.receiveStatus = hasAnyNotFinish
                    ? PurchaseReceiveStatus.RECEIVING
                    : PurchaseReceiveStatus.RECEIVE_FINISHED;
        }
    }
}

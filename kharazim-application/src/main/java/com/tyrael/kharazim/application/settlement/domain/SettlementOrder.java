package com.tyrael.kharazim.application.settlement.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.application.base.BaseDO;
import com.tyrael.kharazim.application.settlement.enums.SettlementOrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Data
public class SettlementOrder extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 结算单编码
     */
    private String code;

    /**
     * 会员编码
     */
    private String customerCode;

    /**
     * 诊所（机构）编码
     */
    private String clinicCode;

    /**
     * 总金额（元）
     */
    private BigDecimal totalAmount;

    /**
     * 来源处方编码
     */
    private String sourcePrescriptionCode;

    /**
     * 结算状态
     */
    private SettlementOrderStatus status;

    /**
     * 结算时间
     */
    private LocalDateTime settlementTime;

    @TableField(exist = false)
    private List<SettlementOrderItem> items;

    public void settlement() {
        this.status = SettlementOrderStatus.PAID;
        this.settlementTime = LocalDateTime.now();
    }

}

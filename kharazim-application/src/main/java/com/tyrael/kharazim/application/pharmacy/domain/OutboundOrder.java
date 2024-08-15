package com.tyrael.kharazim.application.pharmacy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.application.base.BaseDO;
import com.tyrael.kharazim.application.pharmacy.enums.OutboundOrderStatus;
import lombok.Data;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@Data
public class OutboundOrder extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 出库单编码
     */
    private String code;

    /**
     * 出库状态
     */
    private OutboundOrderStatus status;

    /**
     * 来源单据编码
     */
    private String sourceBusinessCode;

    /**
     * 会员编码
     */
    private String customerCode;

    /**
     * 诊所（机构）编码
     */
    private String clinicCode;

    /**
     * 来源单据备注
     */
    private String sourceRemark;

    @TableField(exist = false)
    private List<OutboundOrderItem> items;
}

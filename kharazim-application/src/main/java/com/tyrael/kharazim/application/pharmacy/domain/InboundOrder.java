package com.tyrael.kharazim.application.pharmacy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.application.base.BaseDO;
import com.tyrael.kharazim.application.pharmacy.enums.InboundOrderStatus;
import lombok.Data;

/**
 * 入库单
 *
 * @author Tyrael Archangel
 * @since 2024/7/3
 */
@Data
public class InboundOrder extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 入库单编码
     */
    private String code;

    /**
     * 来源采购单
     */
    private String sourcePurchaseOrderCode;

    /**
     * 诊所编码
     */
    private String clinicCode;

    /**
     * 供应商编码
     */
    private String supplierCode;

    /**
     * 入库状态
     */
    private InboundOrderStatus status;

}

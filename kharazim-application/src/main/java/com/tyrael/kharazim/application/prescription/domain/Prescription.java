package com.tyrael.kharazim.application.prescription.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.application.base.BaseDO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 处方
 *
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
@Data
public class Prescription extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 处方编码
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
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 商品明细
     */
    @TableField(exist = false)
    private List<PrescriptionProduct> products;

    /**
     * 备注
     */
    private String remark;

}

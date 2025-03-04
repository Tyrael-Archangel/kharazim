package com.tyrael.kharazim.basicdata.app.domain.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.basicdata.app.constant.BasicDataDictConstants;
import com.tyrael.kharazim.mybatis.BaseDO;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Tyrael Archangel
 * @since 2024/1/10
 */
@Data
public class CustomerInsurance extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会员编码
     */
    private String customerCode;

    /**
     * 保险公司字典键
     * {@link BasicDataDictConstants#INSURANCE_COMPANY}
     */
    private String companyDict;

    /**
     * 保单号
     */
    private String policyNumber;

    /**
     * 保险有效期限
     */
    private LocalDate duration;

    /**
     * 保险福利
     */
    private String benefits;

    /**
     * 是否为会员的默认保险
     */
    private Boolean defaultInsurance;

}

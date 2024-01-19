package com.tyrael.kharazim.application.customer.vo.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Tyrael Archangel
 * @since 2024/1/16
 */
@Data
@Builder
public class CustomerInsuranceVO {

    @Schema(description = "会员保险ID")
    private Long customerInsuranceId;

    @Schema(description = "保险公司名称")
    private String companyName;

    @Schema(description = "保险公司字典值，字典编码：insurance_company")
    private String companyDictValue;

    @Schema(description = "保单号")
    private String policyNumber;

    @Schema(description = "保险有效期限")
    private LocalDate duration;

    @Schema(description = "保险福利")
    private String benefits;

    @Schema(description = "是否会员默认保险")
    private Boolean defaultInsurance;

}


package com.tyrael.kharazim.application.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCustomerInsuranceRequest {

    @Schema(description = "会员编码")
    @NotBlank(message = "会员编码不能为空")
    private String customerCode;

    @Schema(description = "保险公司字典值，字典编码：insurance_company")
    @NotBlank(message = "请选择保险公司")
    private String companyDictValue;

    @Schema(description = "保单号", maxLength = 64)
    @Size(max = 64, message = "保单号超长")
    private String policyNumber;

    @Schema(description = "保险有效期限，格式yyyy-MM-dd", example = "2023-08-27")
    private LocalDate duration;

    @Schema(description = "保险福利", maxLength = 1024)
    @Size(max = 1024, message = "保险福利超长")
    private String benefits;

    @Schema(description = "是否为会员的默认保险")
    private Boolean defaultInsurance;

    @Schema(hidden = true)
    public boolean isCustomerDefaultInsurance() {
        return Boolean.TRUE.equals(defaultInsurance);
    }

}

package com.tyrael.kharazim.application.customer.vo.customer;

import com.tyrael.kharazim.application.user.enums.UserCertificateTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2024/1/11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyCustomerRequest {

    @Schema(description = "会员名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "性别", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserGenderEnum gender;

    @Schema(description = "生日年")
    @Min(value = 1900, message = "生日年份有误")
    @Max(value = 2100, message = "生日年份有误")
    private Integer birthYear;

    @Schema(description = "生日月")
    @Min(value = 1, message = "生日月份有误")
    @Max(value = 12, message = "生日月份有误")
    private Integer birthMonth;

    @Schema(description = "生日日")
    @Min(value = 1, message = "生日日期有误")
    @Max(value = 31, message = "生日日期有误")
    private Integer birthDayOfMonth;

    @Schema(description = "手机号", maxLength = 32)
    @Size(max = 32, message = "手机号超长")
    private String phone;

    @Schema(description = "证件类型")
    private UserCertificateTypeEnum certificateType;

    @Schema(description = "证件号码", maxLength = 64)
    @Size(max = 64, message = "证件号码超长")
    private String certificateCode;

    @Schema(description = "备注", maxLength = 255)
    @Size(max = 255, message = "会员备注超长")
    private String remark;

}

package com.tyrael.kharazim.basicdata.app.dto.customer.customer;

import com.tyrael.kharazim.basicdata.app.constant.BasicDataDictConstants;
import com.tyrael.kharazim.basicdata.app.enums.CustomerCertificateTypeEnum;
import com.tyrael.kharazim.basicdata.app.enums.CustomerGenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCustomerRequest {

    @Schema(description = "会员名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "请输入会员名")
    private String name;

    @Schema(description = "性别", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "请指定会员性别")
    private CustomerGenderEnum gender;

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
    // @Pattern(regexp = "^(\\+86)?(1[3-9])\\d{9}$", message = "手机号码有误")
    @NotBlank(message = "请输入手机号")
    private String phone;

    @Schema(description = "短信验证码，需要绑定手机号时传入", maxLength = 32)
    private String captcha;

    @Schema(description = "短信验证码序列号，需要绑定手机号时传入", maxLength = 32)
    private String captchaSerialCode;

    /**
     * {@link BasicDataDictConstants#CUSTOMER_CERTIFICATE_TYPE}
     */
    @Schema(description = "证件类型")
    private CustomerCertificateTypeEnum certificateType;

    @Schema(description = "证件号码", maxLength = 64)
    @Size(max = 64, message = "证件号码超长")
    private String certificateCode;

    @Schema(description = "来源渠道字典键，字典编码：customer_source_channel")
    private String sourceChannelDictKey;

    @Schema(description = "推荐（引导）来源会员编码")
    private String sourceCustomerCode;

    @Schema(description = "备注", maxLength = 255)
    @Size(max = 255, message = "会员备注超长")
    private String remark;

    @Schema(description = "专属客服")
    private String serviceUserCode;

    @Schema(description = "专属顾问")
    private String salesConsultantCode;

    @Schema(description = "会员地址")
    private List<AddCustomerAddressRequest> customerAddresses;

    @Schema(description = "会员保险")
    private List<AddCustomerInsuranceRequest> customerInsurances;

}

package com.tyrael.kharazim.application.customer.vo.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2024/1/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BindCustomerPhoneRequest {

    @Schema(description = "会员编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "会员编码不能为空")
    private String customerCode;

    @Schema(description = "手机号", maxLength = 32)
    @Size(max = 32, message = "手机号超长")
    @Pattern(regexp = "^(\\+86)?(1[3-9])\\d{9}$", message = "手机号码有误")
    @NotBlank(message = "绑定手机号不能为空")
    private String phone;

    @Schema(description = "短信验证码", maxLength = 32)
    @NotBlank(message = "短信验证码不能为空")
    private String captcha;

    @Schema(description = "短信验证码序列号", maxLength = 32)
    @NotBlank(message = "短信验证码序列号不能为空")
    private String captchaSerialCode;

}

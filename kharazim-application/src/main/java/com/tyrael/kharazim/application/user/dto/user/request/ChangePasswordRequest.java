package com.tyrael.kharazim.application.user.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Tyrael Archangel
 * @since 2023/12/27
 */
@Data
public class ChangePasswordRequest {

    @Schema(description = "旧密码")
    @NotEmpty(message = "旧密码不能为空")
    private String oldPassword;

    @Schema(description = "新密码")
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, message = "新密码长度不能小于6位")
    @Length(max = 32, message = "新密码长度不能大于32位")
    private String newPassword;

}

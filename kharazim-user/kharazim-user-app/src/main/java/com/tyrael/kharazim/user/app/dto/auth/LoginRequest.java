package com.tyrael.kharazim.user.app.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Tyrael Archangel
 * @since 2023/12/24
 */
@Data
public class LoginRequest {

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, message = "密码长度不能少于6位")
    private String password;

}

package com.tyrael.kharazim.application.user.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2025/1/3
 */
@Data
public class OnlineUserDTO {

    @Schema(description = "token")
    private String token;

    @Schema(description = "用户昵称")
    private String userNickName;

    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

    @Schema(description = "登录主机")
    private String host;

    @Schema(description = "登录地点")
    private String address;

}

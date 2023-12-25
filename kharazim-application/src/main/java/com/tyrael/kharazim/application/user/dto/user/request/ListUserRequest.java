package com.tyrael.kharazim.application.user.dto.user.request;

import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
public class ListUserRequest {

    @Schema(description = "用户名/昵称/手机号")
    private String keywords;

    @Schema(description = "状态，默认ENABLED")
    private EnableStatusEnum status = EnableStatusEnum.ENABLED;

}

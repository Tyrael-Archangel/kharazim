package com.tyrael.kharazim.user.app.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@Builder
@EqualsAndHashCode
public class UserRoleInfo {

    @Schema(description = "用户编码")
    private String userCode;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "用户角色（职位）")
    private String userRoleName;

}

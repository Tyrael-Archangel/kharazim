package com.tyrael.kharazim.application.user.dto.role.request;

import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/4
 */
@Data
public class SaveRoleRequest {

    @Schema(description = "角色（岗位）名")
    @NotEmpty(message = "角色（岗位）名不能为空")
    private String name;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "角色状态(ENABLED-正常；DISABLED-停用)")
    private EnableStatusEnum status;

}
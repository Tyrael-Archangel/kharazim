package com.tyrael.kharazim.application.user.dto.role.response;

import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2024/1/4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色（岗位）详情")
public class RoleDetailDTO {

    @Schema(description = "角色（岗位）ID")
    private Long id;

    @Schema(description = "角色（岗位）编码")
    private String code;

    @Schema(description = "角色（岗位）名")
    private String name;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "启用状态，ENABLED-启用，DISABLED-禁用")
    private EnableStatusEnum status;

}

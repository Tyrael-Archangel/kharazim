package com.tyrael.kharazim.application.user.dto.role.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2024/12/17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色（岗位）信息")
public class RoleDTO {

    @Schema(description = "角色（岗位）编码")
    private String code;

    @Schema(description = "角色（岗位）名")
    private String name;

}

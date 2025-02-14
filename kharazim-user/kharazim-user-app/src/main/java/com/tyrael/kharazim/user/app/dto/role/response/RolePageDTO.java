package com.tyrael.kharazim.user.app.dto.role.response;

import com.tyrael.kharazim.user.app.enums.EnableStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/1/4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色（岗位）")
public class RolePageDTO {

    @Schema(description = "角色（岗位）ID")
    private Long id;

    @Schema(description = "角色（岗位）编码")
    private String code;

    @Schema(description = "角色（岗位）名")
    private String name;

    @Schema(description = "启用状态，1-启用，0-禁用")
    private EnableStatusEnum status;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}

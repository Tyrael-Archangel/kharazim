package com.tyrael.kharazim.application.system.dto.menu;

import com.tyrael.kharazim.application.system.enums.MenuTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
@Data
public class SaveMenuRequest {

    @Schema(description = "上级菜单ID")
    private Long parentId;

    @Schema(description = "菜单名称", maxLength = 64)
    @Size(max = 64, message = "菜单名称超长")
    private String name;

    @Schema(description = "图标", maxLength = 64)
    @Size(max = 64, message = "图标超长")
    private String icon;

    @Schema(description = "路由path", maxLength = 128)
    @Size(max = 128, message = "路由路径超长")
    private String path;

    @Schema(description = "组件路径", maxLength = 128)
    @Size(max = 128, message = "组件路径超长")
    private String component;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "是否可见")
    private Integer visible;

    @Schema(description = "跳转链接", maxLength = 128)
    @Size(max = 128, message = "跳转链接超长")
    private String redirect;

    @Schema(description = MenuTypeEnum.DESC)
    private MenuTypeEnum type;

    @Schema(description = "权限标识码", maxLength = 64)
    @Size(max = 64, message = "权限标识码超长")
    private String perm;

    @Schema(hidden = true)
    public boolean getVisibleValue() {
        // null当做默认可见
        return visible == null || visible == 1;
    }

}

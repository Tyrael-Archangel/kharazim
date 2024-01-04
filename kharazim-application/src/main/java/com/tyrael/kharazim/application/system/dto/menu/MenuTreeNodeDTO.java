package com.tyrael.kharazim.application.system.dto.menu;

import com.tyrael.kharazim.application.system.enums.MenuTypeEnum;
import com.tyrael.kharazim.common.dto.TreeNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/1/4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "菜单树结构")
@EqualsAndHashCode(callSuper = true)
public class MenuTreeNodeDTO extends TreeNode<MenuTreeNodeDTO, Long> {

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "图标")
    private String icon;

    private String component;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "是否可见")
    private Integer visible;

    @Schema(description = "跳转链接")
    private String redirect;

    @Schema(description = MenuTypeEnum.DESC)
    private MenuTypeEnum type;

    @Schema(description = "路由path")
    private String path;

    @Schema(description = "权限标识码")
    private String perm;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(hidden = true)
    public void setVisibleValue(Boolean visibleValue) {
        this.visible = Boolean.TRUE.equals(visibleValue) ? 1 : 0;
    }

}

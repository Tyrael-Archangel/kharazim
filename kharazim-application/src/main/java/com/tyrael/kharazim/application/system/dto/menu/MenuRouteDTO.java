package com.tyrael.kharazim.application.system.dto.menu;

import com.tyrael.kharazim.common.dto.TreeNode;
import lombok.Data;

import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/1/4
 */
@Data
public class MenuRouteDTO extends TreeNode<MenuRouteDTO, Long> {

    private String path;

    private String component;

    private String redirect;

    private String name;

    private Meta meta;

    @Data
    public static class Meta {

        private String title;

        private String icon;

        private Boolean hidden;

        /**
         * 如果设置为 true，目录没有子节点也会显示
         */
        private Boolean alwaysShow;

        /**
         * 页面缓存开启状态
         */
        private Boolean keepAlive;

        private Set<String> roles;

    }
}

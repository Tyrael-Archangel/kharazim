package com.tyrael.kharazim.application.system.service;

import com.tyrael.kharazim.application.system.dto.menu.MenuRouteDTO;
import com.tyrael.kharazim.application.system.dto.menu.MenuTreeNodeDTO;
import com.tyrael.kharazim.application.system.dto.menu.SaveMenuRequest;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
public interface MenuService {

    /**
     * 菜单树
     *
     * @return 菜单树
     */
    List<MenuTreeNodeDTO> menuTree();

    /**
     * 路由列表
     *
     * @return 路由列表
     */
    List<MenuRouteDTO> menuRoutes();

    /**
     * 新增菜单
     *
     * @param addMenuRequest AddMenuRequest
     * @return 新增的菜单ID
     */
    Long add(SaveMenuRequest addMenuRequest);

    /**
     * 修改菜单
     *
     * @param id                菜单ID
     * @param modifyMenuRequest SaveMenuRequest
     */
    void modify(Long id, SaveMenuRequest modifyMenuRequest);

    /**
     * 删除菜单
     *
     * @param ids 菜单ID
     */
    void delete(List<Long> ids);

    /**
     * 禁用菜单
     *
     * @param id 菜单ID
     */
    void disableVisible(Long id);

    /**
     * 启用菜单
     *
     * @param id 菜单ID
     */
    void enableVisible(Long id);

}

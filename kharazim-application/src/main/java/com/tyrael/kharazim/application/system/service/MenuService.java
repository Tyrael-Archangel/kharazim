package com.tyrael.kharazim.application.system.service;

import com.tyrael.kharazim.application.system.dto.menu.SaveMenuRequest;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
public interface MenuService {

    /**
     * 新增菜单
     *
     * @param addMenuRequest AddMenuRequest
     * @return 新增的菜单ID
     */
    Long add(SaveMenuRequest addMenuRequest);

}

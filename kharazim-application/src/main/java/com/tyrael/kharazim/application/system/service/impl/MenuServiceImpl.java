package com.tyrael.kharazim.application.system.service.impl;

import com.tyrael.kharazim.application.system.domain.Menu;
import com.tyrael.kharazim.application.system.dto.menu.SaveMenuRequest;
import com.tyrael.kharazim.application.system.enums.MenuTypeEnum;
import com.tyrael.kharazim.application.system.mapper.MenuMapper;
import com.tyrael.kharazim.application.system.service.MenuService;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.tyrael.kharazim.application.config.CacheKeyConstants.*;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {MENU_RESOURCES, MENU_OPTIONS_TREE, MENU_ROUTES}, allEntries = true)
    public Long add(SaveMenuRequest addMenuRequest) {
        Menu menu = createMenu(addMenuRequest);
        try {
            menuMapper.insert(menu);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("同一级目录下菜单名称不能重复", e);
        }
        return menu.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {MENU_RESOURCES, MENU_OPTIONS_TREE, MENU_ROUTES}, allEntries = true)
    public void modify(Long id, SaveMenuRequest modifyMenuRequest) {
        Menu menu = menuMapper.selectById(id);
        DomainNotFoundException.assertFound(menu, id);

        Menu updateMenu = createMenu(modifyMenuRequest);
        updateMenu.setId(menu.getId());
        updateMenu.setCreateTime(menu.getCreateTime());
        updateMenu.setUpdateTime(LocalDateTime.now());

        try {
            menuMapper.updateById(updateMenu);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("同一级目录下菜单名称不能重复", e);
        }
    }

    private Menu createMenu(SaveMenuRequest addMenuRequest) {
        String path = addMenuRequest.getPath();
        String component = addMenuRequest.getComponent();
        MenuTypeEnum menuType = addMenuRequest.getType();
        if (MenuTypeEnum.CATALOG.equals(menuType)) {
            BusinessException.assertTrue(path.startsWith("/"), "目录路由路径格式错误，必须以/开始");
            component = "Layout";
        } else if (MenuTypeEnum.EXTLINK.equals(menuType)) {
            component = null;
        }

        Menu menu = new Menu();
        Long parentId = addMenuRequest.getParentId();
        menu.setParentId(parentId == null ? -1L : parentId);
        menu.setName(addMenuRequest.getName());
        menu.setIcon(addMenuRequest.getIcon());
        menu.setPath(path);
        menu.setComponent(component);
        menu.setSort(addMenuRequest.getSort());
        menu.setVisible(addMenuRequest.getVisibleValue());
        menu.setRedirect(addMenuRequest.getRedirect());
        menu.setMenuType(menuType);
        menu.setPerm(addMenuRequest.getPerm());
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(menu.getCreateTime());
        return menu;
    }

}

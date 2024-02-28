package com.tyrael.kharazim.application.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.system.domain.Menu;
import com.tyrael.kharazim.application.system.dto.menu.MenuRouteDTO;
import com.tyrael.kharazim.application.system.dto.menu.MenuTreeNodeDTO;
import com.tyrael.kharazim.application.system.dto.menu.SaveMenuRequest;
import com.tyrael.kharazim.application.system.enums.MenuTypeEnum;
import com.tyrael.kharazim.application.system.mapper.MenuMapper;
import com.tyrael.kharazim.application.system.service.MenuService;
import com.tyrael.kharazim.application.user.domain.Role;
import com.tyrael.kharazim.application.user.domain.RoleMenu;
import com.tyrael.kharazim.application.user.mapper.RoleMapper;
import com.tyrael.kharazim.application.user.mapper.RoleMenuMapper;
import com.tyrael.kharazim.common.dto.TreeNode;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.tyrael.kharazim.application.config.CacheKeyConstants.*;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final RoleMapper roleMapper;

    @Override
    public List<MenuTreeNodeDTO> menuTree() {
        List<Menu> menus = menuMapper.listAll();
        List<MenuTreeNodeDTO> menuTreeNodes = menus.stream()
                .map(e -> {
                    MenuTreeNodeDTO menuTreeNodeDTO = new MenuTreeNodeDTO();
                    menuTreeNodeDTO.setId(e.getId());
                    menuTreeNodeDTO.setParentId(e.getParentId());
                    menuTreeNodeDTO.setName(e.getName());
                    menuTreeNodeDTO.setIcon(e.getIcon());
                    menuTreeNodeDTO.setComponent(e.getComponent());
                    menuTreeNodeDTO.setSort(e.getSort());
                    menuTreeNodeDTO.setVisibleValue(e.getVisible());
                    menuTreeNodeDTO.setRedirect(e.getRedirect());
                    menuTreeNodeDTO.setType(e.getMenuType());
                    menuTreeNodeDTO.setPath(e.getPath());
                    menuTreeNodeDTO.setPerm(e.getPerm());
                    menuTreeNodeDTO.setCreateTime(e.getCreateTime());
                    menuTreeNodeDTO.setUpdateTime(e.getUpdateTime());
                    return menuTreeNodeDTO;
                })
                .toList();
        return TreeNode.build(menuTreeNodes);
    }

    @Override
    @Cacheable(cacheNames = MENU_ROUTES)
    public List<MenuRouteDTO> menuRoutes() {
        List<Menu> menus = menuMapper.listAll();
        Map<Long, Set<String>> menuRoleCodesMap = menuRoles();
        return buildMenuRouteTree(menus, menuRoleCodesMap);
    }

    private Map<Long, Set<String>> menuRoles() {
        List<RoleMenu> roleMenus = roleMenuMapper.listAll();
        if (roleMenus.isEmpty()) {
            return Maps.newHashMap();
        }
        List<Long> roleIds = roleMenus.stream()
                .map(RoleMenu::getRoleId)
                .toList();
        List<Role> roles = roleMapper.selectBatchIds(roleIds);
        Map<Long, Role> roleMap = roles.stream()
                .collect(Collectors.toMap(Role::getId, e -> e));

        Map<Long, List<RoleMenu>> menuRoleGroup = roleMenus.stream()
                .collect(Collectors.groupingBy(RoleMenu::getMenuId));

        Map<Long, Set<String>> menuRoleCodesMap = Maps.newHashMap();
        menuRoleGroup.forEach((menuId, rms) -> {
            Set<String> menuRoleCodes = rms.stream()
                    .map(rm -> roleMap.get(rm.getRoleId()))
                    .filter(Objects::nonNull)
                    .map(Role::getCode)
                    .collect(Collectors.toSet());
            menuRoleCodesMap.put(menuId, menuRoleCodes);
        });

        return menuRoleCodesMap;
    }

    private List<MenuRouteDTO> buildMenuRouteTree(List<Menu> menus, Map<Long, Set<String>> menuRoles) {
        List<MenuRouteDTO> menuRoutes = menus.stream()
                .filter(e -> !MenuTypeEnum.BUTTON.equals(e.getMenuType()))
                .map(menu -> {
                    MenuRouteDTO menuRouteDTO = new MenuRouteDTO();
                    menuRouteDTO.setId(menu.getId());
                    menuRouteDTO.setParentId(menu.getParentId());
                    menuRouteDTO.setPath(menu.getPath());
                    menuRouteDTO.setComponent(menu.getComponent());
                    menuRouteDTO.setRedirect(menu.getRedirect());
                    menuRouteDTO.setName(menu.getName());

                    MenuRouteDTO.Meta meta = new MenuRouteDTO.Meta();
                    meta.setTitle(menu.getName());
                    meta.setIcon(menu.getIcon());
                    meta.setHidden(!Boolean.TRUE.equals(menu.getVisible()));
                    meta.setKeepAlive(true);
                    Set<String> roleCodes = menuRoles.getOrDefault(menu.getId(), Sets.newHashSet());
                    roleCodes.add(Role.SUPER_ADMIN_CODE);
                    meta.setRoles(roleCodes);

                    menuRouteDTO.setMeta(meta);

                    return menuRouteDTO;
                })
                .toList();

        List<MenuRouteDTO> treeMenuRoutes = TreeNode.build(menuRoutes);
        for (MenuRouteDTO menuRoute : menuRoutes) {
            MenuRouteDTO.Meta meta = menuRoute.getMeta();
            Collection<MenuRouteDTO> children = menuRoute.getChildren();
            boolean alwaysShow = CollUtil.isNotEmpty(children)
                    && children.stream().anyMatch(child -> Boolean.FALSE.equals(child.getMeta().getHidden()));
            meta.setAlwaysShow(alwaysShow);
        }
        return treeMenuRoutes;
    }

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {MENU_RESOURCES, MENU_OPTIONS_TREE, MENU_ROUTES}, allEntries = true)
    public void delete(List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            menuMapper.deleteBatchIds(ids);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {MENU_RESOURCES, MENU_OPTIONS_TREE, MENU_ROUTES}, allEntries = true)
    public void disableVisible(Long id) {
        Menu menu = menuMapper.selectById(id);
        DomainNotFoundException.assertFound(menu, id);
        menu.setVisible(Boolean.FALSE);
        menu.setUpdateTime(LocalDateTime.now());
        menuMapper.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {MENU_RESOURCES, MENU_OPTIONS_TREE, MENU_ROUTES}, allEntries = true)
    public void enableVisible(Long id) {
        Menu menu = menuMapper.selectById(id);
        DomainNotFoundException.assertFound(menu, id);
        menu.setVisible(Boolean.TRUE);
        menu.setUpdateTime(LocalDateTime.now());
        menuMapper.updateById(menu);
    }

}

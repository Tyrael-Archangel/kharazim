package com.tyrael.kharazim.application.system.service.impl;

import com.tyrael.kharazim.application.system.domain.Menu;
import com.tyrael.kharazim.application.system.dto.menu.MenuTreeNodeDTO;
import com.tyrael.kharazim.application.system.dto.menu.SaveMenuRequest;
import com.tyrael.kharazim.application.system.enums.MenuTypeEnum;
import com.tyrael.kharazim.application.system.mapper.MenuMapper;
import com.tyrael.kharazim.application.system.service.MenuService;
import com.tyrael.kharazim.common.dto.TreeNode;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;

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
        List<MenuTreeNodeDTO> menuTree = TreeNode.build(menuTreeNodes);
        this.setFullPathName(menuTree, null);
        return menuTree;
    }

    private void setFullPathName(Collection<MenuTreeNodeDTO> tree,
                                 MenuTreeNodeDTO parent) {
        if (tree == null || tree.isEmpty()) {
            return;
        }
        String parentFullPathName = Optional.ofNullable(parent)
                .map(MenuTreeNodeDTO::getFullPathName)
                .map(String::trim)
                .orElse("");
        for (MenuTreeNodeDTO dto : tree) {
            if (parentFullPathName.isEmpty()) {
                dto.setFullPathName(dto.getName());
            } else {
                dto.setFullPathName(parentFullPathName + " / " + dto.getName());
            }
            setFullPathName(dto.getChildren(), dto);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(SaveMenuRequest addMenuRequest) {

        if (addMenuRequest.getSort() == null) {
            addMenuRequest.setSort(menuMapper.selectMaxSort());
        }

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
    public void delete(Long id) {
        menuMapper.deleteById(id);
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
    public void disableVisible(Long id) {
        Menu menu = menuMapper.selectById(id);
        DomainNotFoundException.assertFound(menu, id);
        menu.setVisible(Boolean.FALSE);
        menu.setUpdateTime(LocalDateTime.now());
        menuMapper.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableVisible(Long id) {
        Menu menu = menuMapper.selectById(id);
        DomainNotFoundException.assertFound(menu, id);
        menu.setVisible(Boolean.TRUE);
        menu.setUpdateTime(LocalDateTime.now());
        menuMapper.updateById(menu);
    }

}

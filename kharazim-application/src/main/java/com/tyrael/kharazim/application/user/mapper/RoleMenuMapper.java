package com.tyrael.kharazim.application.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.tyrael.kharazim.application.user.domain.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Tyrael Archangel
 * @since 2024/1/4
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * list by roleIds
     *
     * @param roleIds roleIds
     * @return roleMenus
     */
    default List<RoleMenu> listByRoleIds(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<RoleMenu> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(RoleMenu::getRoleId, roleIds);
        return selectList(queryWrapper);
    }

    /**
     * list by roleId
     *
     * @param roleId roleId
     * @return roleMenus
     */
    default List<RoleMenu> listByRoleId(Long roleId) {
        return listByRoleIds(Collections.singletonList(roleId));
    }

    /**
     * 保存角色（岗位）的菜单
     *
     * @param roleId  角色（岗位）ID
     * @param menuIds 菜单ID
     */
    default void save(Long roleId, List<Long> menuIds) {

        this.deleteByRoleId(roleId);

        if (!CollectionUtils.isEmpty(menuIds)) {
            List<RoleMenu> roleMenus = menuIds.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .map(menuId -> {
                        RoleMenu roleMenu = new RoleMenu();
                        roleMenu.setRoleId(roleId);
                        roleMenu.setMenuId(menuId);
                        return roleMenu;
                    }).toList();
            if (!roleMenus.isEmpty()) {
                Db.saveBatch(roleMenus);
            }
        }
    }

    /**
     * 删除角色（岗位）的菜单
     *
     * @param roleId 角色（岗位）ID
     */
    default void deleteByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleMenu> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(RoleMenu::getRoleId, roleId);
        this.delete(queryWrapper);
    }

    /**
     * list all
     *
     * @return all records
     */
    default List<RoleMenu> listAll() {
        return selectList(Wrappers.lambdaQuery());
    }

}

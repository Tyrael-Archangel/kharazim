package com.tyrael.kharazim.user.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.lib.base.util.CollectionUtils;
import com.tyrael.kharazim.user.app.domain.Role;
import com.tyrael.kharazim.user.app.domain.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * list by userId
     *
     * @param userId 用户ID
     * @return entities
     */
    default List<UserRole> listByUserId(Long userId) {
        return listByUserIds(List.of(userId));
    }

    /**
     * list by userIds
     *
     * @param userIds 用户ID
     * @return entities
     */
    default List<UserRole> listByUserIds(Collection<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<UserRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(UserRole::getUserId, userIds);
        return selectList(queryWrapper);
    }

    /**
     * 保存用户角色
     *
     * @param userId 用户ID
     * @param roles  角色
     */
    default void save(Long userId, List<Role> roles) {
        this.deleteByUserId(userId);
        if (CollectionUtils.isNotEmpty(roles)) {
            for (Role role : roles) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(role.getId());
                this.insert(userRole);
            }
        }
    }

    /**
     * 删除用户角色
     *
     * @param userId 用户ID
     */
    default void deleteByUserId(Long userId) {
        LambdaQueryWrapper<UserRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserRole::getUserId, userId);
        this.delete(queryWrapper);
    }

}

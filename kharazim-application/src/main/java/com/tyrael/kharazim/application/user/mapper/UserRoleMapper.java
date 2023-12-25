package com.tyrael.kharazim.application.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.user.domain.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.CollectionUtils;

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
     * find by userId
     *
     * @param userId 用户ID
     * @return entities
     */
    default UserRole findByUserId(Long userId) {
        LambdaQueryWrapper<UserRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserRole::getUserId, userId);
        return selectOne(queryWrapper);
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
     * @param roleId 角色ID
     */
    default void save(Long userId, Long roleId) {
        this.deleteByUserId(userId);
        if (roleId != null) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            this.insert(userRole);
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

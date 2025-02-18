package com.tyrael.kharazim.user.app.service;

import com.tyrael.kharazim.user.app.dto.user.response.UserRoleDTO;

import java.util.Collection;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/27
 */
public interface UserRoleQueryService {

    /**
     * 获取用户的角色
     *
     * @param userIds 用户ID
     * @return 用户的角色
     */
    List<UserRoleDTO> queryUserRoles(Collection<Long> userIds);

    /**
     * 获取用户的角色
     *
     * @param userId 用户ID
     * @return 用户的角色
     */
    default List<UserRoleDTO> queryUserRoles(Long userId) {
        return queryUserRoles(List.of(userId));
    }

}

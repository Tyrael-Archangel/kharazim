package com.tyrael.kharazim.application.user.service;


import com.tyrael.kharazim.application.user.dto.user.response.UserRoleDTO;

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
     * @param userCodes 用户编码
     * @return 用户的角色
     */
    List<UserRoleDTO> queryUserRolesByUserCodes(Collection<String> userCodes);

}

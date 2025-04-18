package com.tyrael.kharazim.user.app.service.impl;

import com.tyrael.kharazim.user.app.domain.Role;
import com.tyrael.kharazim.user.app.domain.UserRole;
import com.tyrael.kharazim.user.app.dto.user.response.UserRoleDTO;
import com.tyrael.kharazim.user.app.mapper.RoleMapper;
import com.tyrael.kharazim.user.app.mapper.UserRoleMapper;
import com.tyrael.kharazim.user.app.service.UserRoleQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2023/12/27
 */
@Service
@RequiredArgsConstructor
public class UserRoleQueryServiceImpl implements UserRoleQueryService {

    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;

    @Override
    public List<UserRoleDTO> queryUserRoles(Collection<Long> userIds) {
        List<UserRole> userRoles = userRoleMapper.listByUserIds(userIds);
        Set<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toSet());
        List<Role> roles = roleMapper.selectByIds(roleIds);
        Map<Long, Role> roleMap = roles.stream()
                .collect(Collectors.toMap(Role::getId, e -> e));
        return userRoles.stream()
                .map(userRole -> {
                    Long roleId = userRole.getRoleId();
                    Role role = roleMap.get(roleId);
                    return UserRoleDTO.builder()
                            .userId(userRole.getUserId())
                            .roleId(roleId)
                            .roleCode(role.getCode())
                            .roleName(role.getName())
                            .admin(role.getSuperAdmin())
                            .build();
                })
                .collect(Collectors.toList());
    }

}

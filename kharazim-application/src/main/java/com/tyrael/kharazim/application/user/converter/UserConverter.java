package com.tyrael.kharazim.application.user.converter;

import com.google.common.collect.Lists;
import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.system.service.FileService;
import com.tyrael.kharazim.application.user.domain.Role;
import com.tyrael.kharazim.application.user.domain.User;
import com.tyrael.kharazim.application.user.dto.user.response.CurrentUserDTO;
import com.tyrael.kharazim.application.user.dto.user.response.UserDTO;
import com.tyrael.kharazim.application.user.dto.user.response.UserRoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Component
@RequiredArgsConstructor
public class UserConverter {

    private final FileService fileService;

    /**
     * User、UserRoleDTO -> UserDTO
     */
    public UserDTO userDTO(User user, UserRoleDTO userRole) {
        return UserDTO.builder()
                .id(user.getId())
                .code(user.getCode())
                .name(user.getName())
                .nickName(user.getNickName())
                .englishName(user.getEnglishName())
                .avatar(user.getAvatar())
                .avatarUrl(fileService.getUrl(user.getAvatar()))
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .phone(user.getPhone())
                .status(user.getStatus())
                .wechatCode(user.getWechatCode())
                .wechatName(user.getWechatName())
                .certificateType(user.getCertificateType())
                .certificateCode(user.getCertificateCode())
                .remark(user.getRemark())
                .roleId(userRole == null ? null : userRole.getRoleId())
                .roleCode(userRole == null ? null : userRole.getRoleCode())
                .roleName(userRole == null ? null : userRole.getRoleName())
                .creator(user.getCreator())
                .creatorCode(user.getCreatorCode())
                .createTime(user.getCreateTime())
                .updater(user.getUpdater())
                .updaterCode(user.getUpdaterCode())
                .updateTime(user.getUpdateTime())
                .build();
    }

    /**
     * User、UserRoleDTO -> CurrentUserDTO
     */
    public CurrentUserDTO currentUserDTO(User user, UserRoleDTO role, LocalDateTime lastLoginTime) {
        String roleName = null;
        List<String> roleCodes = Lists.newArrayList();
        if (role != null) {
            roleName = role.getRoleName();
            roleCodes.add(role.getRoleCode());
        }
        return CurrentUserDTO.builder()
                .id(user.getId())
                .code(user.getCode())
                .name(user.getName())
                .nickName(user.getNickName())
                .englishName(user.getEnglishName())
                .avatar(user.getAvatar())
                .avatarUrl(fileService.getUrl(user.getAvatar()))
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .phone(user.getPhone())
                .roleName(roleName)
                .roles(roleCodes)
                .needChangePassword(user.getNeedChangePassword())
                .lastLogin(lastLoginTime)
                .build();
    }

    /**
     * User,Role -> AuthUser
     */
    public AuthUser authUser(User user, Role role) {
        AuthUser authUser = new AuthUser();
        authUser.setId(user.getId());
        authUser.setCode(user.getCode());
        authUser.setName(user.getName());
        authUser.setNickName(user.getNickName());
        authUser.setSuperAdmin(role != null && role.isAdmin());
        return authUser;
    }

}

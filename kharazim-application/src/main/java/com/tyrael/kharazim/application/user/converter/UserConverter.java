package com.tyrael.kharazim.application.user.converter;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.system.service.FileService;
import com.tyrael.kharazim.application.user.domain.User;
import com.tyrael.kharazim.application.user.dto.role.response.RoleDTO;
import com.tyrael.kharazim.application.user.dto.user.response.CurrentUserDTO;
import com.tyrael.kharazim.application.user.dto.user.response.UserDTO;
import com.tyrael.kharazim.application.user.dto.user.response.UserRoleDTO;
import com.tyrael.kharazim.common.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public UserDTO userDTO(User user, List<UserRoleDTO> userRoles) {
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
                .roles(this.roles(userRoles))
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
    public CurrentUserDTO currentUserDTO(User user, List<UserRoleDTO> userRoles) {
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
                .roles(this.roles(userRoles))
                .needChangePassword(user.getNeedChangePassword())
                .build();
    }

    private List<RoleDTO> roles(List<UserRoleDTO> userRoles) {
        if (CollectionUtils.isEmpty(userRoles)) {
            return new ArrayList<>();
        }
        return userRoles.stream()
                .map(e -> RoleDTO.builder()
                        .code(e.getRoleCode())
                        .name(e.getRoleName())
                        .build()
                ).collect(Collectors.toList());
    }

    /**
     * User -> AuthUser
     */
    public AuthUser authUser(User user) {

        AuthUser authUser = new AuthUser();
        authUser.setId(user.getId());
        authUser.setCode(user.getCode());
        authUser.setName(user.getName());
        authUser.setNickName(user.getNickName());
        return authUser;
    }

}

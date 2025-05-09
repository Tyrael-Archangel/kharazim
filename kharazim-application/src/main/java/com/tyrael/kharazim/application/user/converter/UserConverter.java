package com.tyrael.kharazim.application.user.converter;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.user.domain.User;
import com.tyrael.kharazim.application.user.dto.auth.LoginClientInfo;
import com.tyrael.kharazim.application.user.dto.auth.OnlineUserDTO;
import com.tyrael.kharazim.application.user.dto.role.response.RoleDTO;
import com.tyrael.kharazim.application.user.dto.user.response.CurrentUserDTO;
import com.tyrael.kharazim.application.user.dto.user.response.UserDTO;
import com.tyrael.kharazim.application.user.dto.user.response.UserRoleDTO;
import com.tyrael.kharazim.application.user.service.component.TokenManager;
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

    /**
     * TokenManager.LoggedUser -> OnlineUserDTO
     */
    public OnlineUserDTO onlineUser(TokenManager.RefreshLoggedUser loggedUser, User user) {
        OnlineUserDTO onlineUser = new OnlineUserDTO();
        onlineUser.setToken(loggedUser.getToken());
        onlineUser.setUserCode(user.getCode());
        onlineUser.setUsername(user.getName());
        onlineUser.setUserNickName(user.getNickName());
        onlineUser.setUserAvatar(user.getAvatar());
        onlineUser.setLoginTime(loggedUser.getLoggedTime());
        onlineUser.setLastRefreshTime(loggedUser.getLastRefreshTime());
        LoginClientInfo loginClientInfo = loggedUser.getLoginClientInfo();
        if (loginClientInfo != null) {
            onlineUser.setHost(loginClientInfo.getHost());
            onlineUser.setOs(loginClientInfo.getOs());
            onlineUser.setBrowser(loginClientInfo.getBrowser());
            onlineUser.setBrowserVersion(loginClientInfo.getBrowserVersion());
        }
        return onlineUser;
    }

}

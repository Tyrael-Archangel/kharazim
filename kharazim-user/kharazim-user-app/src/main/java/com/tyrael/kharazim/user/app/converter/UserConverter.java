package com.tyrael.kharazim.user.app.converter;

import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.sdk.service.FileServiceApi;
import com.tyrael.kharazim.user.app.domain.User;
import com.tyrael.kharazim.user.app.dto.auth.OnlineUserDTO;
import com.tyrael.kharazim.user.app.dto.role.response.RoleDTO;
import com.tyrael.kharazim.user.app.dto.user.response.CurrentUserDTO;
import com.tyrael.kharazim.user.app.dto.user.response.UserDTO;
import com.tyrael.kharazim.user.app.dto.user.response.UserRoleDTO;
import com.tyrael.kharazim.user.app.service.component.TokenManager;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import com.tyrael.kharazim.user.sdk.vo.ClientInfo;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
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

    @DubboReference
    private FileServiceApi fileService;

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
    public AuthUser authUser(User user, String token) {
        AuthUser authUser = new AuthUser();
        authUser.setId(user.getId());
        authUser.setCode(user.getCode());
        authUser.setName(user.getName());
        authUser.setNickName(user.getNickName());
        authUser.setToken(token);
        return authUser;
    }

    /**
     * TokenManager.LoggedUser -> OnlineUserDTO
     */
    public OnlineUserDTO onlineUser(TokenManager.RefreshLoggedUser loggedUser, User user) {
        OnlineUserDTO onlineUser = new OnlineUserDTO();
        onlineUser.setToken(loggedUser.token());
        onlineUser.setUserCode(user.getCode());
        onlineUser.setUsername(user.getName());
        onlineUser.setUserNickName(user.getNickName());
        onlineUser.setUserAvatar(user.getAvatar());
        onlineUser.setUserAvatarUrl(fileService.getUrl(user.getAvatar()));
        onlineUser.setLoginTime(loggedUser.getLoggedTime());
        onlineUser.setLastRefreshTime(loggedUser.getLastRefreshTime());
        ClientInfo clientInfo = loggedUser.getClientInfo();
        if (clientInfo != null) {
            onlineUser.setHost(clientInfo.getHost());
            onlineUser.setOs(clientInfo.getOs());
            onlineUser.setBrowser(clientInfo.getBrowser());
            onlineUser.setBrowserVersion(clientInfo.getBrowserVersion());
        }
        return onlineUser;
    }

}

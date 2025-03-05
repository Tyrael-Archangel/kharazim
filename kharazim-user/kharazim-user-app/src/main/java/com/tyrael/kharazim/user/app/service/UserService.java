package com.tyrael.kharazim.user.app.service;

import com.tyrael.kharazim.authentication.Principal;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.user.app.dto.user.request.*;
import com.tyrael.kharazim.user.app.dto.user.response.CurrentUserDTO;
import com.tyrael.kharazim.user.app.dto.user.response.UserDTO;
import com.tyrael.kharazim.user.app.enums.EnableStatusEnum;

import java.util.Collection;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/27
 */
public interface UserService {

    /**
     * Get by ID
     *
     * @param id ID
     * @return UserDTO
     */
    UserDTO getById(Long id);

    /**
     * list by codes
     *
     * @param codes codes
     * @return users
     */
    List<UserDTO> listByCodes(Collection<String> codes);

    /**
     * Get current user info
     *
     * @param currentUser 当前登录用户
     * @return CurrentUserDTO
     */
    CurrentUserDTO getCurrentUserInfo(Principal currentUser);

    /**
     * page user
     *
     * @param pageCommand PageUserRequest
     * @return Page of users
     */
    PageResponse<UserDTO> page(PageUserRequest pageCommand);

    /**
     * list user
     *
     * @param listRequest {@link ListUserRequest}
     * @return Users
     */
    List<UserDTO> list(ListUserRequest listRequest);

    /**
     * add user
     *
     * @param addUserRequest AddUserRequest
     * @return 用户随机密码
     */
    String add(AddUserRequest addUserRequest);

    /**
     * 修改用户
     *
     * @param modifyUserRequest ModifyUserRequest
     * @param currentUser       操作人
     */
    void modify(ModifyUserRequest modifyUserRequest, Principal currentUser);

    /**
     * 修改用户密码
     *
     * @param currentUser           CurrentUser
     * @param changePasswordRequest UpdatePasswordRequest
     */
    void changePassword(Principal currentUser, ChangePasswordRequest changePasswordRequest);

    /**
     * 重置密码
     *
     * @param currentUser 当前用户
     * @param userId      重置密码目标用户
     * @return newPassword
     */
    String resetPassword(Principal currentUser, Long userId);

    /**
     * 修改用户状态
     *
     * @param id          ID
     * @param status      状态
     * @param currentUser CurrentUser
     */
    void updateStatus(Long id, EnableStatusEnum status, Principal currentUser);

}

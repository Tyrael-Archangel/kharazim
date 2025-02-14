package com.tyrael.kharazim.user.app.service;

import com.tyrael.kharazim.lib.base.dto.PageCommand;
import com.tyrael.kharazim.user.app.dto.auth.LoginRequest;
import com.tyrael.kharazim.user.app.dto.auth.OnlineUserDTO;
import com.tyrael.kharazim.user.sdk.exception.LoginFailedException;
import com.tyrael.kharazim.user.sdk.exception.TokenInvalidException;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/24
 */
public interface AuthService {

    /**
     * login
     *
     * @param loginRequest       LoginRequest
     * @param httpServletRequest HttpServletRequest
     * @return token
     * @throws LoginFailedException 用户名或密码不对
     */
    String safetyLogin(LoginRequest loginRequest, HttpServletRequest httpServletRequest) throws LoginFailedException;

    /**
     * logout
     *
     * @param token token
     */
    void logout(String token);

    /**
     * logout by userId
     *
     * @param userId userId
     */
    void logoutByUser(Long userId);

    /**
     * verify token
     *
     * @param token token
     * @return AuthUser
     * @throws TokenInvalidException token invalid
     */
    AuthUser verifyToken(String token) throws TokenInvalidException;

    /**
     * 在线用户信息
     *
     * @param pageCommand PageCommand
     * @return 在线用户信息
     */
    List<OnlineUserDTO> onlineUsers(PageCommand pageCommand);

}

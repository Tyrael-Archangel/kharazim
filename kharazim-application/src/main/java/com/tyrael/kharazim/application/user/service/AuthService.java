package com.tyrael.kharazim.application.user.service;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.user.dto.auth.LoginRequest;
import com.tyrael.kharazim.application.user.dto.auth.OnlineUserDTO;
import com.tyrael.kharazim.common.exception.LoginFailedException;
import com.tyrael.kharazim.common.exception.TokenInvalidException;
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
     * @return 在线用户信息
     */
    List<OnlineUserDTO> onlineUsers();

}

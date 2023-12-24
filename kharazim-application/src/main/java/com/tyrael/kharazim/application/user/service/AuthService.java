package com.tyrael.kharazim.application.user.service;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.user.dto.auth.LoginRequest;
import com.tyrael.kharazim.common.exception.LoginFailedException;
import com.tyrael.kharazim.common.exception.TokenInvalidException;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

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
     * 用户上一次登录的时间
     *
     * @param userId userId
     * @return 用户上一次登录的时间
     */
    LocalDateTime getUserLastLoginTime(Long userId);

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

}

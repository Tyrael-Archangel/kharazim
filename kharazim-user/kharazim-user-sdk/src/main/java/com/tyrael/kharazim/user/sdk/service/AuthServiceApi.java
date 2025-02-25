package com.tyrael.kharazim.user.sdk.service;

import com.tyrael.kharazim.base.exception.UnauthorizedException;
import com.tyrael.kharazim.user.sdk.exception.TokenInvalidException;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import com.tyrael.kharazim.user.sdk.vo.ClientInfo;

/**
 * @author Tyrael Archangel
 * @since 2025/2/11
 */
public interface AuthServiceApi {

    /**
     * verify token
     *
     * @param token token
     * @return AuthUser
     * @throws TokenInvalidException token invalid
     */
    AuthUser verifyToken(String token) throws TokenInvalidException;

    /**
     * login
     *
     * @param username   username
     * @param password   password
     * @param clientInfo ClientInfo
     * @return token
     * @throws UnauthorizedException UnauthorizedException
     */
    String login(String username, String password, ClientInfo clientInfo) throws UnauthorizedException;

}

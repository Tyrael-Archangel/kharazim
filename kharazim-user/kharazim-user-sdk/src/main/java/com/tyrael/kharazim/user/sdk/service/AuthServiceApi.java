package com.tyrael.kharazim.user.sdk.service;

import com.tyrael.kharazim.user.sdk.exception.TokenInvalidException;
import com.tyrael.kharazim.user.sdk.model.AuthUser;

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

}

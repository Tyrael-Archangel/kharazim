package com.tyrael.kharazim.user.sdk.exception;

import com.tyrael.kharazim.lib.base.exception.UnauthorizedException;

/**
 * @author Tyrael Archangel
 * @since 2023/12/22
 */
public class TokenInvalidException extends UnauthorizedException {

    public TokenInvalidException(String message) {
        super(message);
    }

}

package com.tyrael.kharazim.user.sdk.exception;

import com.tyrael.kharazim.base.exception.UnauthorizedException;

/**
 * @author Tyrael Archangel
 * @since 2023/12/22
 */
public class LoginFailedException extends UnauthorizedException {

    public LoginFailedException(String message) {
        super(message);
    }

}
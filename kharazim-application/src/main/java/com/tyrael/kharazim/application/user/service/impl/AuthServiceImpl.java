package com.tyrael.kharazim.application.user.service.impl;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.user.dto.auth.LoginRequest;
import com.tyrael.kharazim.application.user.service.AuthService;
import com.tyrael.kharazim.common.exception.LoginFailedException;
import com.tyrael.kharazim.common.exception.TokenInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2023/12/24
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Override
    public String safetyLogin(LoginRequest loginRequest, HttpServletRequest httpServletRequest) throws LoginFailedException {
        return null;
    }

    @Override
    public LocalDateTime getUserLastLoginTime(Long userId) {
        return null;
    }

    @Override
    public void logout(String token) {

    }

    @Override
    public void logoutByUser(Long userId) {

    }

    @Override
    public AuthUser verifyToken(String token) throws TokenInvalidException {
        return null;
    }
}

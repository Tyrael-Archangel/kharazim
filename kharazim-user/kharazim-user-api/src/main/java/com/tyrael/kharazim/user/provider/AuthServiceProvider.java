package com.tyrael.kharazim.user.provider;

import com.tyrael.kharazim.base.exception.UnauthorizedException;
import com.tyrael.kharazim.user.app.dto.auth.LoginRequest;
import com.tyrael.kharazim.user.app.service.AuthService;
import com.tyrael.kharazim.user.sdk.exception.TokenInvalidException;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import com.tyrael.kharazim.user.sdk.service.AuthServiceApi;
import com.tyrael.kharazim.user.sdk.vo.ClientInfo;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Tyrael Archangel
 * @since 2025/2/11
 */
@DubboService
@RequiredArgsConstructor
public class AuthServiceProvider implements AuthServiceApi {

    private final AuthService authService;

    @Override
    public AuthUser verifyToken(String token) throws TokenInvalidException {
        return authService.verifyToken(token);
    }

    @Override
    public String login(String username, String password, ClientInfo clientInfo) throws UnauthorizedException {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName(username);
        loginRequest.setPassword(password);
        return authService.safetyLogin(loginRequest, clientInfo);
    }

}

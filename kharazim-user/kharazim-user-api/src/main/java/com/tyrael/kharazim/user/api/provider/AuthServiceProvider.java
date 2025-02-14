package com.tyrael.kharazim.user.api.provider;

import com.tyrael.kharazim.user.sdk.exception.TokenInvalidException;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import com.tyrael.kharazim.user.sdk.service.AuthServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Tyrael Archangel
 * @since 2025/2/11
 */
@DubboService
@RequiredArgsConstructor
public class AuthServiceProvider implements AuthServiceApi {

    @Override
    public AuthUser verifyToken(String token) throws TokenInvalidException {
        AuthUser authUser = new AuthUser();
        authUser.setId(1L);
        authUser.setCode("000000");
        authUser.setName("admin");
        authUser.setNickName("admin");
        return authUser;
    }

}

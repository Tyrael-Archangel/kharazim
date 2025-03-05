package com.tyrael.kharazim.user.api.sdk.config;

import com.tyrael.kharazim.authentication.PrincipalHolder;
import com.tyrael.kharazim.user.sdk.constant.UserHeader;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Tyrael Archangel
 * @since 2023/12/24
 */
public class AuthUserResolverInterceptor implements WebRequestInterceptor {

    @Override
    public void preHandle(@NonNull WebRequest webRequest) {

        String userId = this.utf8Decode(webRequest.getHeader(UserHeader.USER_ID));

        if (StringUtils.hasText(userId)) {
            String userCode = this.utf8Decode(webRequest.getHeader(UserHeader.USER_CODE));
            String userName = this.utf8Decode(webRequest.getHeader(UserHeader.USER_NAME));
            String userNickname = this.utf8Decode(webRequest.getHeader(UserHeader.USER_NICKNAME));
            String token = this.utf8Decode(webRequest.getHeader(UserHeader.TOKEN));

            AuthUser authUser = new AuthUser();
            authUser.setId(Long.parseLong(userId));
            authUser.setCode(userCode);
            authUser.setName(userName);
            authUser.setNickName(userNickname);
            authUser.setToken(token);
            PrincipalHolder.setPrincipal(authUser);
        }
    }

    @Override
    public void postHandle(@NonNull WebRequest webRequest, @Nullable ModelMap model) {
        // do nothing
    }

    @Override
    public void afterCompletion(@NonNull WebRequest webRequest, @Nullable Exception ex) {
        PrincipalHolder.remove();
    }

    private String utf8Decode(String value) {
        if (value == null) {
            return null;
        }
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

}

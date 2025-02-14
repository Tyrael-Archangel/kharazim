package com.tyrael.kharazim.user.api.sdk.handler;

import com.tyrael.kharazim.user.sdk.model.AuthUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUserHolder {

    /**
     * hold current thread user and token
     */
    private static final ThreadLocal<AuthUserAndToken> AUTH_USER_THREAD_LOCAL = new ThreadLocal<>();

    public static AuthUser getCurrentUser() {
        AuthUserAndToken authUserAndToken = AUTH_USER_THREAD_LOCAL.get();
        return authUserAndToken == null ? null : authUserAndToken.authUser();
    }

    public static Long getCurrentUserId() {
        AuthUserAndToken authUserAndToken = AUTH_USER_THREAD_LOCAL.get();
        return authUserAndToken == null ? null : authUserAndToken.authUser().getId();
    }

    public static String getCurrentUserToken() {
        AuthUserAndToken authUserAndToken = AUTH_USER_THREAD_LOCAL.get();
        return authUserAndToken == null ? null : authUserAndToken.token();
    }

    public static void setCurrentUser(AuthUser authUser, String token) {
        AUTH_USER_THREAD_LOCAL.set(new AuthUserAndToken(authUser, token));
    }

    public static void remove() {
        AUTH_USER_THREAD_LOCAL.remove();
    }

    record AuthUserAndToken(AuthUser authUser, String token) {
    }
}

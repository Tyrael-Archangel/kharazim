package com.tyrael.kharazim.application.base.auth;

import com.alibaba.fastjson.JSON;
import com.tyrael.kharazim.application.user.service.AuthService;
import com.tyrael.kharazim.common.dto.ExceptionResponse;
import com.tyrael.kharazim.common.exception.TokenInvalidException;
import com.tyrael.kharazim.web.config.SystemGlobalConfig;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Enumeration;

/**
 * @author Tyrael Archangel
 * @since 2023/12/24
 */
@Component
public class GlobalAuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final SystemGlobalConfig systemGlobalConfig;
    private final RequestPathMatcher whiteListPatternMatcher;

    public GlobalAuthInterceptor(AuthService authService, SystemGlobalConfig systemGlobalConfig, AuthConfig authConfig) {
        this.authService = authService;
        this.systemGlobalConfig = systemGlobalConfig;
        this.whiteListPatternMatcher = new RequestPathMatcher(authConfig.getWhiteList());
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws IOException {
        String token = getToken(request);

        ExceptionResponse exceptionResponse;
        try {
            AuthUser authUser = authService.verifyToken(token);
            CurrentUserHolder.setCurrentUser(authUser, token);
            return true;
        } catch (TokenInvalidException e) {
            exceptionResponse = new ExceptionResponse(e, HttpStatus.UNAUTHORIZED.value(), "登录已失效，请重新登录", systemGlobalConfig.isEnablePrintExceptionStackTrace());
        } catch (Exception e) {
            exceptionResponse = new ExceptionResponse(e, HttpStatus.INTERNAL_SERVER_ERROR.value(), systemGlobalConfig.isEnablePrintExceptionStackTrace());
        }

        if (whiteListPatternMatcher.matches(request)) {
            // 白名单
            return true;
        } else {

            response.setStatus(exceptionResponse.getCode());
            response.addHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(exceptionResponse));
            request.getSession().invalidate();

            return false;
        }
    }

    private String getToken(HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        if (!StringUtils.hasText(token)) {
            token = getTokenFromParam(request);
        }
        if (!StringUtils.hasText(token)) {
            token = getTokenFromCookie(request);
        }
        return token;
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader(AuthConfig.TOKEN_HEADER);
    }

    private String getTokenFromParam(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames != null && parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            if (AuthConfig.TOKEN_HEADER.equalsIgnoreCase(parameterName)) {
                return request.getParameter(parameterName);
            }
        }
        return null;
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (AuthConfig.TOKEN_HEADER.equalsIgnoreCase(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        CurrentUserHolder.remove();
    }

}

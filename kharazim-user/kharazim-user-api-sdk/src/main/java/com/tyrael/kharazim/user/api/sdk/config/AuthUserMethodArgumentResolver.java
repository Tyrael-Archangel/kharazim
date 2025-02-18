package com.tyrael.kharazim.user.api.sdk.config;

import com.tyrael.kharazim.base.exception.UnauthorizedException;
import com.tyrael.kharazim.user.api.sdk.annotation.CurrentUser;
import com.tyrael.kharazim.user.api.sdk.handler.AuthUserHolder;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
public class AuthUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (!parameter.getParameterType().isAssignableFrom(AuthUser.class)) {
            return false;
        }
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof CurrentUser) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  @Nullable ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  @Nullable WebDataBinderFactory binderFactory) {
        AuthUser authUser = AuthUserHolder.getCurrentUser();
        if (authUser != null) {
            return authUser;
        }

        CurrentUser currentUser = parameter.getMethodAnnotation(CurrentUser.class);
        if (currentUser != null && currentUser.required()) {
            throw new UnauthorizedException("can not resolve current user");
        } else {
            return null;
        }
    }

}

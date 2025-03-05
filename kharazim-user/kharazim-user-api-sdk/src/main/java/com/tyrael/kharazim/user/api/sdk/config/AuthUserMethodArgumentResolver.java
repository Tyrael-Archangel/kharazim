package com.tyrael.kharazim.user.api.sdk.config;

import com.tyrael.kharazim.authentication.Principal;
import com.tyrael.kharazim.authentication.PrincipalHolder;
import com.tyrael.kharazim.base.exception.UnauthorizedException;
import com.tyrael.kharazim.authentication.CurrentPrincipal;
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
        if (!parameter.getParameterType().isAssignableFrom(Principal.class)) {
            return false;
        }
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof CurrentPrincipal) {
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
        Principal principal = PrincipalHolder.getPrincipal();
        if (principal != null) {
            return principal;
        }

        CurrentPrincipal currentPrincipal = parameter.getMethodAnnotation(CurrentPrincipal.class);
        if (currentPrincipal != null && currentPrincipal.required()) {
            throw new UnauthorizedException("can not resolve current user");
        } else {
            return null;
        }
    }

}

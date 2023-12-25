package com.tyrael.kharazim.web.config;

import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.extend.filter.basic.JakartaServletSecurityBasicAuthFilter;
import com.github.xiaoymin.knife4j.extend.util.FilterUtils;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jHttpBasic;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jProperties;
import com.tyrael.kharazim.application.user.dto.auth.LoginRequest;
import com.tyrael.kharazim.application.user.service.AuthService;
import com.tyrael.kharazim.common.exception.UnauthorizedException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.EqualsAndHashCode;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public JakartaServletSecurityBasicAuthFilter servletSecurityBasicAuthFilter(
            ObjectProvider<Knife4jProperties> knife4jProperties,
            ObjectProvider<SpringDocConfigProperties> springDocConfigProperties,
            AuthService authService) {
        Knife4jProperties properties = knife4jProperties.getIfAvailable();
        boolean forbidden = properties == null
                || springDocConfigProperties.getIfAvailable() == null
                || !properties.isEnable();
        Knife4jBasicAuthFilter authFilter = new Knife4jBasicAuthFilter(forbidden, authService);
        Optional.ofNullable(properties)
                .map(Knife4jProperties::getBasic)
                .map(Knife4jHttpBasic::getInclude)
                .ifPresent(authFilter::addRule);
        return authFilter;
    }

    @EqualsAndHashCode(callSuper = false)
    public static class Knife4jBasicAuthFilter extends JakartaServletSecurityBasicAuthFilter implements Filter {

        private final boolean forbidden;
        private final AuthService authService;

        public Knife4jBasicAuthFilter(boolean forbidden, AuthService authService) {
            this.forbidden = forbidden;
            this.authService = authService;
        }

        @Override
        public void init(FilterConfig filterConfig) {
            // do nothing
        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            boolean requestMatch = super.match(request.getRequestURI());
            if (!requestMatch) {
                chain.doFilter(servletRequest, servletResponse);
                return;
            }

            if (forbidden) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.getWriter().write(HttpStatus.NOT_FOUND.toString());
                return;
            }
            if (request.getSession().getAttribute(GlobalConstants.KNIFE4J_BASIC_AUTH_SESSION) == null) {

                try {

                    String auth = request.getHeader(GlobalConstants.AUTH_HEADER_NAME);
                    String token = this.tryAuth(auth, request);

                    request.getSession().setAttribute(GlobalConstants.KNIFE4J_BASIC_AUTH_SESSION, token);
                    response.addCookie(new Cookie(SystemGlobalConfig.TOKEN_HEADER, token));

                } catch (UnauthorizedException e) {

                    FilterUtils.writeJakartaForbiddenCode(response);
                    return;
                }
            }

            chain.doFilter(servletRequest, servletResponse);
        }

        private String tryAuth(String auth, HttpServletRequest request) {
            if (!StringUtils.hasText(auth)) {
                throw new UnauthorizedException("Unauthorized");
            }

            String userNameAndPassword = decodeBase64(auth.substring(6));
            String[] userNameAndPasswordArray = userNameAndPassword.split(":");

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUserName(userNameAndPasswordArray[0]);
            loginRequest.setPassword(userNameAndPasswordArray[1]);

            return authService.safetyLogin(loginRequest, request);
        }

        @Override
        public void destroy() {
            this.urlFilters = null;
        }
    }

}

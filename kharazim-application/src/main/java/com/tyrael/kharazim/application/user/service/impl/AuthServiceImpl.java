package com.tyrael.kharazim.application.user.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUserHolder;
import com.tyrael.kharazim.application.config.CacheKeyConstants;
import com.tyrael.kharazim.application.user.domain.Role;
import com.tyrael.kharazim.application.user.domain.User;
import com.tyrael.kharazim.application.user.domain.UserRole;
import com.tyrael.kharazim.application.user.dto.auth.LoginRequest;
import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import com.tyrael.kharazim.application.user.mapper.RoleMapper;
import com.tyrael.kharazim.application.user.mapper.UserMapper;
import com.tyrael.kharazim.application.user.mapper.UserRoleMapper;
import com.tyrael.kharazim.application.user.service.AuthService;
import com.tyrael.kharazim.application.user.service.component.PasswordEncoder;
import com.tyrael.kharazim.application.user.service.component.TokenManager;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.LoginFailedException;
import com.tyrael.kharazim.common.exception.TokenInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tyrael Archangel
 * @since 2023/12/24
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final Cache<String, AtomicInteger> addressLoginFailedCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(5))
            .maximumSize(200)
            .build();

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenManager tokenManager;
    private final CacheManager cacheManager;

    @Override
    public String safetyLogin(LoginRequest loginRequest, HttpServletRequest httpServletRequest) throws LoginFailedException {

        String remoteAddress = getRemoteAddress(httpServletRequest);
        checkViolentLogin(remoteAddress);

        try {
            String token = this.login(loginRequest);
            clearLoginFailedCache(remoteAddress);
            return token;
        } catch (LoginFailedException e) {
            saveLoginFailedCache(remoteAddress);
            throw e;
        }
    }

    private String login(LoginRequest loginRequest) throws LoginFailedException {

        String userName = loginRequest.getUserName();
        String requestPassword = loginRequest.getPassword();
        User user = userMapper.findByName(userName);
        if (user == null) {
            throw new LoginFailedException("用户名或密码错误");
        }
        if (!EnableStatusEnum.ENABLED.equals(user.getStatus())) {
            throw new LoginFailedException("用户被禁用");
        }

        String userPassword = user.getPassword();

        boolean matches = passwordEncoder.matches(requestPassword, userPassword);
        if (matches) {
            this.clearCurrentUserInfoCache(user.getId());
            return tokenManager.create(user, findUserRole(user));
        } else {
            throw new LoginFailedException("用户名或密码错误");
        }
    }

    private void checkViolentLogin(String address) {
        AtomicInteger failedCount = addressLoginFailedCache.getIfPresent(address);
        if (failedCount != null && failedCount.get() > 5) {
            throw new BusinessException("尝试登录失败次数过多，请稍后再试");
        }
    }

    private void clearLoginFailedCache(String address) {
        addressLoginFailedCache.invalidate(address);
    }

    private void saveLoginFailedCache(String address) {
        AtomicInteger failedCount = addressLoginFailedCache.get(address, e -> new AtomicInteger(0));
        failedCount.incrementAndGet();
    }

    private String getRemoteAddress(HttpServletRequest httpServletRequest) {
        String address = httpServletRequest.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(address) || "unknown".equalsIgnoreCase(address)) {
            address = httpServletRequest.getHeader("X-Real-IP");
        }
        if (StringUtils.isBlank(address) || "unknown".equalsIgnoreCase(address)) {
            address = httpServletRequest.getRemoteAddr();
        }
        if (address.contains(",")) {
            return address.split(",")[0];
        }
        return address;
    }

    @Override
    public LocalDateTime getUserLastLoginTime(Long userId) {
        return tokenManager.lastAuth(userId);
    }

    private Role findUserRole(User user) {
        UserRole userRole = userRoleMapper.findByUserId(user.getId());
        if (userRole == null) {
            return null;
        }
        return roleMapper.selectById(userRole.getRoleId());
    }

    @Override
    public void logout(String token) {
        tokenManager.remove(token);
        clearCurrentUserInfoCache(CurrentUserHolder.getCurrentUserId());
    }

    private void clearCurrentUserInfoCache(Long userId) {
        if (userId != null) {
            org.springframework.cache.Cache currentUserInfoCache = cacheManager.getCache(CacheKeyConstants.CURRENT_USER_INFO);
            if (currentUserInfoCache != null) {
                currentUserInfoCache.evict(userId);
            }
        }
    }

    @Override
    public void logoutByUser(Long userId) {
        if (userId != null) {
            tokenManager.removeByUser(userId);
        }
    }

    @Override
    public AuthUser verifyToken(String token) throws TokenInvalidException {
        return tokenManager.verifyToken(token);
    }
}

package com.tyrael.kharazim.application.user.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.config.cache.CacheKeyConstants;
import com.tyrael.kharazim.application.user.converter.UserConverter;
import com.tyrael.kharazim.application.user.domain.User;
import com.tyrael.kharazim.application.user.dto.auth.LoginClientInfo;
import com.tyrael.kharazim.application.user.dto.auth.LoginRequest;
import com.tyrael.kharazim.application.user.dto.auth.OnlineUserDTO;
import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import com.tyrael.kharazim.application.user.mapper.UserMapper;
import com.tyrael.kharazim.application.user.service.AuthService;
import com.tyrael.kharazim.application.user.service.component.PasswordEncoder;
import com.tyrael.kharazim.application.user.service.component.TokenManager;
import com.tyrael.kharazim.common.dto.PageCommand;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.LoginFailedException;
import com.tyrael.kharazim.common.exception.TokenInvalidException;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tyrael Archangel
 * @since 2023/12/24
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenManager tokenManager;
    private final CacheManager cacheManager;
    private final UserConverter userConverter;

    private final ViolentLoginManager violentLoginManager = new ViolentLoginManager();

    @Override
    public String safetyLogin(LoginRequest loginRequest, HttpServletRequest httpServletRequest) throws LoginFailedException {

        LoginClientInfo loginClientInfo = getClientInfo(httpServletRequest);
        String host = loginClientInfo.getHost();
        violentLoginManager.checkViolentLogin(host);

        try {
            String token = this.login(loginRequest, loginClientInfo);
            violentLoginManager.clearLoginFailedCache(host);
            return token;
        } catch (LoginFailedException e) {
            violentLoginManager.saveLoginFailedCache(host);
            throw e;
        }
    }

    private String login(LoginRequest loginRequest, LoginClientInfo loginClientInfo) throws LoginFailedException {

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
            return tokenManager.create(userConverter.authUser(user), loginClientInfo);
        } else {
            throw new LoginFailedException("用户名或密码错误");
        }
    }

    private LoginClientInfo getClientInfo(HttpServletRequest request) {

        String host = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(host) || "unknown".equalsIgnoreCase(host)) {
            host = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isBlank(host) || "unknown".equalsIgnoreCase(host)) {
            host = request.getRemoteAddr();
        }
        if (host.contains(",")) {
            host = host.split(",")[0];
        }

        LoginClientInfo loginClientInfo = new LoginClientInfo();
        loginClientInfo.setHost(host);

        UserAgent userAgent;
        try {
            userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        } catch (Exception e) {
            return loginClientInfo;
        }

        loginClientInfo.setBrowser(userAgent.getBrowser().getName());
        loginClientInfo.setOs(userAgent.getOperatingSystem().getName());
        loginClientInfo.setBrowserVersion(Objects.toString(userAgent.getBrowserVersion()));
        return loginClientInfo;
    }

    @Override
    public void logout(String token) {
        Long userId = tokenManager.remove(token);
        clearCurrentUserInfoCache(userId);
    }

    private void clearCurrentUserInfoCache(Long userId) {
        if (userId != null) {
            Cache currentUserInfoCache = cacheManager.getCache(CacheKeyConstants.CURRENT_USER_INFO);
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

    @Override
    public List<OnlineUserDTO> onlineUsers(PageCommand pageCommand) {
        List<TokenManager.LoggedUser> loggedUsers = tokenManager.loggedUsers(pageCommand);
        List<Long> userIds = loggedUsers.stream()
                .map(e -> e.getAuthUser().getId())
                .toList();
        List<User> users = userMapper.selectBatchIds(userIds);

        return userConverter.onlineUsers(loggedUsers, users);
    }

    static class ViolentLoginManager {

        private final com.github.benmanes.caffeine.cache.Cache<String, AtomicInteger> addressLoginFailedCache;

        public ViolentLoginManager() {
            this.addressLoginFailedCache = Caffeine.newBuilder()
                    .expireAfterWrite(Duration.ofMinutes(5))
                    .maximumSize(200)
                    .build();
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

    }

}

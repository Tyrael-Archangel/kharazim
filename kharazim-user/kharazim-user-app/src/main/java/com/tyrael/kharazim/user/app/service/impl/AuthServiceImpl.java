package com.tyrael.kharazim.user.app.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.user.app.config.CacheKeyConstants;
import com.tyrael.kharazim.user.app.converter.UserConverter;
import com.tyrael.kharazim.user.app.domain.User;
import com.tyrael.kharazim.user.sdk.vo.ClientInfo;
import com.tyrael.kharazim.user.app.dto.auth.LoginRequest;
import com.tyrael.kharazim.user.app.dto.auth.OnlineUserDTO;
import com.tyrael.kharazim.user.app.enums.EnableStatusEnum;
import com.tyrael.kharazim.user.app.mapper.UserMapper;
import com.tyrael.kharazim.user.app.service.AuthService;
import com.tyrael.kharazim.user.app.service.component.PasswordEncoder;
import com.tyrael.kharazim.user.app.service.component.TokenManager;
import com.tyrael.kharazim.user.sdk.exception.LoginFailedException;
import com.tyrael.kharazim.user.sdk.exception.TokenInvalidException;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
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
    public String safetyLogin(LoginRequest loginRequest, ClientInfo clientInfo) throws LoginFailedException {
        String host = clientInfo.getHost();
        violentLoginManager.checkViolentLogin(host);

        try {
            String token = this.login(loginRequest, clientInfo);
            violentLoginManager.clearLoginFailedCache(host);
            return token;
        } catch (LoginFailedException e) {
            violentLoginManager.saveLoginFailedCache(host);
            throw e;
        }
    }

    private String login(LoginRequest loginRequest, ClientInfo clientInfo) throws LoginFailedException {

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
            return tokenManager.create(userConverter.authUser(user), clientInfo);
        } else {
            throw new LoginFailedException("用户名或密码错误");
        }
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
        List<TokenManager.RefreshLoggedUser> loggedUsers = tokenManager.loggedUsers(pageCommand);
        List<String> userCodes = loggedUsers.stream()
                .map(e -> e.getAuthUser().getCode())
                .toList();
        Map<String, User> userMap = userMapper.mapByCodes(userCodes);
        return loggedUsers.stream()
                .map(loggedUser -> userConverter.onlineUser(
                        loggedUser,
                        userMap.get(loggedUser.getAuthUser().getCode()))
                )
                .toList();
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

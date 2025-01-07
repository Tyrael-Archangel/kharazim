package com.tyrael.kharazim.application.user.service.component;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.base.auth.AuthConfig;
import com.tyrael.kharazim.application.base.auth.AuthTokenConfig;
import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.user.dto.auth.LoginClientInfo;
import com.tyrael.kharazim.common.dto.PageCommand;
import com.tyrael.kharazim.common.exception.TokenInvalidException;
import com.tyrael.kharazim.common.util.CollectionUtils;
import com.tyrael.kharazim.common.util.RandomStringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenManager {

    private final TokenRefresher tokenRefresher = new TokenRefresher();

    private final StringRedisTemplate redisTemplate;
    private final AuthTokenConfig authTokenConfig;
    private final AuthConfig authConfig;

    /**
     * create token for user
     */
    public String create(AuthUser user, LoginClientInfo loginClientInfo) {

        if (!authConfig.isAllowMultiLogin()) {
            this.removeByUser(user.getId());
        }

        String token = RandomStringUtil.make(32);
        LoggedUser loggedUser = new LoggedUser(token, user, loginClientInfo, LocalDateTime.now());

        redisTemplate.opsForValue().set(
                this.tokenCacheKey(token),
                loggedUser.toJson(),
                authTokenConfig.getTokenExpire());
        redisTemplate.opsForValue().set(
                this.userTokenCacheKey(user.getId(), token),
                loggedUser.getLoggedTime().toString(),
                authTokenConfig.getTokenExpire());

        return token;
    }

    /**
     * remove token
     */
    public Long remove(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        Long userId = null;
        String loggedUserJson = getLoggedUserJson(token);
        List<String> tokenKeyAndUserTokenKey = Lists.newArrayList(this.tokenCacheKey(token));
        if (StringUtils.hasText(loggedUserJson)) {
            LoggedUser loggedUser = LoggedUser.parse(loggedUserJson);
            if (loggedUser != null) {
                userId = loggedUser.authUser.getId();
                tokenKeyAndUserTokenKey.add(this.userTokenCacheKey(userId, token));
            }
        }
        redisTemplate.delete(tokenKeyAndUserTokenKey);
        return userId;
    }

    /**
     * remove by userId
     */
    public void removeByUser(Long userId) {
        String matchUserTokenCachePattern = this.userTokenCacheKeyPattern(userId);
        ScanOptions scanOptions = ScanOptions.scanOptions().match(matchUserTokenCachePattern).build();
        Set<String> keys;
        try (Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
            keys = cursor.stream().collect(Collectors.toSet());
        }
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        Set<String> tokenCacheKeys = keys.stream()
                .map(e -> e.substring(matchUserTokenCachePattern.length()))
                .filter(StringUtils::hasText)
                .map(this::tokenCacheKey)
                .collect(Collectors.toSet());

        redisTemplate.delete(Sets.union(keys, tokenCacheKeys));
    }

    private String tokenCacheKey(String token) {
        return authTokenConfig.getTokenCachePrefix() + ":" + token;
    }

    private String tokenCacheKeyPattern() {
        return authTokenConfig.getTokenCachePrefix() + ":*";
    }

    private String userTokenCacheKey(Long userId, String token) {
        return authTokenConfig.getUserTokenCachePrefix() + ":" + userId + ":" + token;
    }

    private String userTokenCacheKeyPattern(Long userId) {
        return authTokenConfig.getUserTokenCachePrefix() + ":" + userId + ":*";
    }

    /**
     * verify token and return AuthUser
     *
     * @param token token
     * @return AuthUser
     * @throws TokenInvalidException token empty or invalid
     */
    public AuthUser verifyToken(String token) throws TokenInvalidException {
        if (!StringUtils.hasText(token)) {
            throw new TokenInvalidException("token is empty");
        }

        String loggedUserJson = this.getLoggedUserJson(token);
        if (!StringUtils.hasText(loggedUserJson)) {
            throw new TokenInvalidException("invalid token: " + token);
        }

        LoggedUser loggedUser = LoggedUser.parse(loggedUserJson);
        AuthUser authUser = loggedUser == null ? null : loggedUser.authUser;
        if (authUser != null && authTokenConfig.isAutoRefreshExpire()) {
            tokenRefresher.refreshExpire(authUser.getId(), token);
        }
        return authUser;
    }

    private String getLoggedUserJson(String token) {
        return redisTemplate.opsForValue()
                .get(this.tokenCacheKey(token));
    }

    /**
     * list all logged users
     */
    public List<LoggedUser> loggedUsers(PageCommand pageCommand) {
        ScanOptions scanOptions = ScanOptions.scanOptions().match(tokenCacheKeyPattern()).build();
        Set<String> keys;
        try (Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
            keys = cursor.stream().collect(Collectors.toSet());
        }
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        List<String> loggedUserJsons = redisTemplate.opsForValue()
                .multiGet(keys);
        if (CollectionUtils.isEmpty(loggedUserJsons)) {
            return Collections.emptyList();
        }

        return loggedUserJsons.stream()
                .map(LoggedUser::parse)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(LoggedUser::getLoggedTime).reversed())
                .skip((pageCommand.getPageIndex() - 1L) * pageCommand.getPageSize())
                .limit(pageCommand.getPageSize())
                .toList();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoggedUser {

        private String token;
        private AuthUser authUser;
        private LoginClientInfo loginClientInfo;
        private LocalDateTime loggedTime;

        public static LoggedUser parse(String json) {
            if (!StringUtils.hasText(json)) {
                return null;
            }

            try {
                return JSON.parseObject(json, LoggedUser.class);
            } catch (Exception e) {
                log.warn("resolve LoggedUser from loggedUserJson error: {}", e.getMessage(), e);
                return null;
            }
        }

        public String toJson() {
            return JSON.toJSONString(this);
        }

    }

    /**
     * 刷新token，5分钟内相同的token只刷新一次
     */
    private class TokenRefresher {

        private final Cache<String, Boolean> cache;

        public TokenRefresher() {
            this.cache = Caffeine.newBuilder()
                    .expireAfterWrite(Duration.ofMinutes(5L))
                    .maximumSize(100L)
                    .build();
        }

        public void refreshExpire(Long userId, String token) {
            Boolean value = cache.getIfPresent(token);
            if (value == null) {
                cache.put(token, Boolean.TRUE);
                redisTemplate.expire(tokenCacheKey(token), authTokenConfig.getTokenExpire());
                redisTemplate.expire(userTokenCacheKey(userId, token), authTokenConfig.getTokenExpire());
            }
        }

    }

}

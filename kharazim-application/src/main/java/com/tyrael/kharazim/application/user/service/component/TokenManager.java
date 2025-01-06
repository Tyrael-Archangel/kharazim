package com.tyrael.kharazim.application.user.service.component;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.base.auth.AuthConfig;
import com.tyrael.kharazim.application.base.auth.AuthTokenConfig;
import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.common.exception.TokenInvalidException;
import com.tyrael.kharazim.common.util.CollectionUtils;
import com.tyrael.kharazim.common.util.RandomStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenManager {

    private static final String CACHE_KEY_SEP = ":";

    private final TokenRefresher tokenRefresher = new TokenRefresher();

    private final StringRedisTemplate redisTemplate;
    private final AuthTokenConfig authTokenConfig;
    private final AuthConfig authConfig;

    /**
     * create token for user
     */
    public String create(AuthUser user) {

        if (!authConfig.isAllowMultiLogin()) {
            this.removeByUser(user.getId());
        }

        String token = RandomStringUtil.make(32);

        String authUserJson = JSON.toJSONString(user);

        redisTemplate.opsForValue().set(
                this.tokenCacheKey(token),
                authUserJson,
                authTokenConfig.getTokenExpire());
        redisTemplate.opsForValue().set(
                this.userTokenCacheKey(user.getId(), token),
                String.valueOf(System.currentTimeMillis()),
                authTokenConfig.getTokenExpire());

        return token;
    }

    /**
     * remove token
     */
    public void remove(String token) {
        if (!StringUtils.hasText(token)) {
            return;
        }
        String authUserJson = getAuthUserJson(token);
        List<String> tokenKeyAndUserTokenKey = Lists.newArrayList(this.tokenCacheKey(token));
        if (StringUtils.hasText(authUserJson)) {
            try {
                AuthUser authUser = JSON.parseObject(authUserJson, AuthUser.class);
                if (authUser != null) {
                    tokenKeyAndUserTokenKey.add(this.userTokenCacheKey(authUser.getId(), token));
                }
            } catch (Exception e) {
                log.warn("resolve AuthUser from authUserJson error: {}", e.getMessage(), e);
            }
        }
        redisTemplate.delete(tokenKeyAndUserTokenKey);
    }

    /**
     * remove by userId
     */
    public void removeByUser(Long userId) {
        String matchUserTokenCachePattern = this.userTokenCacheKeyPrefix(userId) + "*";
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
        return authTokenConfig.getTokenCachePrefix() + CACHE_KEY_SEP + token;
    }

    private String userTokenCacheKey(Long userId, String token) {
        return userTokenCacheKeyPrefix(userId) + CACHE_KEY_SEP + token;
    }

    private String userTokenCacheKeyPrefix(Long userId) {
        return authTokenConfig.getUserTokenCachePrefix() + CACHE_KEY_SEP + userId;
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

        String authUserJson = this.getAuthUserJson(token);
        if (!StringUtils.hasText(authUserJson)) {
            throw new TokenInvalidException("invalid token: " + token);
        }

        AuthUser authUser = JSON.parseObject(authUserJson, AuthUser.class);
        if (authUser != null && authTokenConfig.isAutoRefreshExpire()) {
            tokenRefresher.refreshExpire(authUser.getId(), token);
        }
        return authUser;
    }

    private String getAuthUserJson(String token) {
        return redisTemplate.opsForValue()
                .get(this.tokenCacheKey(token));
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

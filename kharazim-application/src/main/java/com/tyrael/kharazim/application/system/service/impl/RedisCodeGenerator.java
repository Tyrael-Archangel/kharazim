package com.tyrael.kharazim.application.system.service.impl;

import com.tyrael.kharazim.common.exception.ShouldNotHappenException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "system.global.code-generator", havingValue = "redis")
public class RedisCodeGenerator extends AbstractCodeGenerator {

    private static final String CODE_PREFIX = "CODE_ALLOCATE";
    private final StringRedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        log.info("Use Redis as CodeGenerator");
    }

    @Override
    public long increment(String tag) {

        String key = CODE_PREFIX + ":" + tag;

        Long increment = redisTemplate.opsForValue()
                .increment(key);
        if (increment == null) {
            throw new ShouldNotHappenException("increase error, tag: " + tag);
        } else {
            return increment;
        }
    }

}

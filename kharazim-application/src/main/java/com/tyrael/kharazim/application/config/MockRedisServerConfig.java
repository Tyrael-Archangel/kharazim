package com.tyrael.kharazim.application.config;

import com.github.fppt.jedismock.RedisServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 创建模拟的 Redis Server 服务器
 *
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@Configuration
@ConditionalOnClass(RedisServer.class)
@ConditionalOnProperty(value = "system.global.use-inner-mock-redis", havingValue = "true")
public class MockRedisServerConfig {

    @Bean
    public RedisServer redisServer(RedisProperties properties) throws IOException {
        RedisServer redisServer = new RedisServer(properties.getPort());
        redisServer.start();
        return redisServer;
    }

}

package com.tyrael.kharazim.cache;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties({CacheProperties.class, RedisCacheProperties.class})
@ConditionalOnProperty(value = "spring.cache.type", havingValue = "REDIS")
public class RedisCustomizerConfiguration {

    @Bean
    public RedisCacheConfiguration defaultRedisCacheConfiguration(CacheProperties cacheProperties) {

        JsonMapper jsonMapper = JsonMapper.builder()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .addModule(new JavaTimeModule())
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .activateDefaultTyping(
                        LaissezFaireSubTypeValidator.instance,
                        ObjectMapper.DefaultTyping.NON_FINAL,
                        JsonTypeInfo.As.PROPERTY)
                .configure(MapperFeature.USE_ANNOTATIONS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.isUseKeyPrefix() && redisProperties.getKeyPrefix() != null) {
            config = config.computePrefixWith(name ->
                    redisProperties.getKeyPrefix() + ":" + name + ":");
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }

        return config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer.UTF_8))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(jsonMapper)));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(RedisCacheProperties redisCacheProperties,
                                                                                 RedisCacheConfiguration defaultRedisCacheConfiguration) {
        return builder -> {
            builder.cacheDefaults(defaultRedisCacheConfiguration);
            List<CacheKeyProperties> caches = redisCacheProperties.getCaches();
            if (!CollectionUtils.isEmpty(caches)) {
                for (CacheKeyProperties cacheKeyProperties : caches) {
                    RedisCacheConfiguration cacheKeyConfiguration = defaultRedisCacheConfiguration.entryTtl(cacheKeyProperties.getTtl());
                    builder.withCacheConfiguration(cacheKeyProperties.getName(), cacheKeyConfiguration);
                }
            }
        };
    }

}

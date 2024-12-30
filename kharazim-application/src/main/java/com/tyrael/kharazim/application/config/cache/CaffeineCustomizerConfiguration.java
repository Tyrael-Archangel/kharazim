package com.tyrael.kharazim.application.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties({CacheProperties.class, CaffeineCacheProperties.class})
@ConditionalOnProperty(value = "spring.cache.type", havingValue = "CAFFEINE")
public class CaffeineCustomizerConfiguration {

    @Bean
    public Caffeine<Object, Object> caffeine(CaffeineCacheProperties caffeineCacheProperties) {
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if (caffeineCacheProperties.getDefaultTtl() != null) {
            cacheBuilder.expireAfterWrite(caffeineCacheProperties.getDefaultTtl());
        }
        return cacheBuilder;
    }

    @Bean
    public CacheManagerCustomizer<CaffeineCacheManager> customerCacheManagerCustomizer(CaffeineCacheProperties caffeineCacheProperties) {
        return cacheManager -> {
            List<CacheKeyProperties> caches = caffeineCacheProperties.getCaches();
            if (caches == null || caches.isEmpty()) {
                return;
            }

            for (CacheKeyProperties cacheKeyProperties : caches) {

                Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder();
                if (cacheKeyProperties.getTtl() != null) {
                    caffeineBuilder.expireAfterWrite(cacheKeyProperties.getTtl());
                } else if (caffeineCacheProperties.getDefaultTtl() != null) {
                    caffeineBuilder.expireAfterWrite(caffeineCacheProperties.getDefaultTtl());
                }

                if (cacheKeyProperties.getCapacity() != null) {
                    caffeineBuilder.maximumSize(cacheKeyProperties.getCapacity());
                }

                cacheManager.registerCustomCache(cacheKeyProperties.getName(), caffeineBuilder.build());
            }
        };
    }

}

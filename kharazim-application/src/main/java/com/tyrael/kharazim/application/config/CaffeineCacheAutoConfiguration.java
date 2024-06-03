package com.tyrael.kharazim.application.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
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
public class CaffeineCacheAutoConfiguration {

    @Bean
    public CacheManagerCustomizers cacheManagerCustomizers(ObjectProvider<CacheManagerCustomizer<?>> customizers) {
        return new CacheManagerCustomizers(customizers.orderedStream().toList());
    }

    @Bean("caffeineCacheManager")
    public CaffeineCacheManager caffeineCacheManager(CacheManagerCustomizers customizers,
                                                     ObjectProvider<CacheLoader<Object, Object>> cacheLoader,
                                                     CaffeineCacheProperties caffeineCacheProperties) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(this.cacheBuilder(caffeineCacheProperties));
        cacheLoader.ifAvailable(cacheManager::setCacheLoader);

        setCustomerCache(cacheManager, caffeineCacheProperties);

        return customizers.customize(cacheManager);
    }

    private Caffeine<Object, Object> cacheBuilder(CaffeineCacheProperties caffeineCacheProperties) {
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if (caffeineCacheProperties.getDefaultTtl() != null) {
            cacheBuilder.expireAfterWrite(caffeineCacheProperties.getDefaultTtl());
        }
        return cacheBuilder;
    }

    private void setCustomerCache(CaffeineCacheManager cacheManager, CaffeineCacheProperties caffeineCacheProperties) {

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
    }

}

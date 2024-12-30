package com.tyrael.kharazim.application.config.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
@Data
@ConfigurationProperties(prefix = "spring.cache.caffeine")
public class CaffeineCacheProperties {

    private Duration defaultTtl;

    private List<CacheKeyProperties> caches = new ArrayList<>();

}

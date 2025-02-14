package com.tyrael.kharazim.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jay Yang
 * @since 2024/12/30
 */
@Data
@ConfigurationProperties(prefix = "spring.cache.redis")
public class RedisCacheProperties {

    private List<CacheKeyProperties> caches = new ArrayList<>();

}


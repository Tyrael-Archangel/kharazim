package com.tyrael.kharazim.application.config.cache;

import lombok.Data;

import java.time.Duration;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
@Data
public class CacheKeyProperties {

    /**
     * 缓存key名称
     */
    private String name;

    /**
     * 过期时间
     */
    private Duration ttl;

    /**
     * 容量
     */
    private Integer capacity;

}

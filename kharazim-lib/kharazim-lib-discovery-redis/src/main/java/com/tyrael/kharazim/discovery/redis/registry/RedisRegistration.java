package com.tyrael.kharazim.discovery.redis.registry;

import com.tyrael.kharazim.discovery.redis.RedisDiscoveryProperties;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.serviceregistry.Registration;

import java.net.URI;
import java.util.Map;

/**
 * @author Tyrael Archangel
 * @since 2025/3/7
 */
public class RedisRegistration implements Registration {

    private final RedisDiscoveryProperties redisDiscoveryProperties;

    public RedisRegistration(RedisDiscoveryProperties redisDiscoveryProperties) {
        this.redisDiscoveryProperties = redisDiscoveryProperties;
    }

    @Override
    public String getInstanceId() {
        return redisDiscoveryProperties.getInstanceId();
    }

    @Override
    public String getServiceId() {
        return redisDiscoveryProperties.getService();
    }

    @Override
    public String getHost() {
        return redisDiscoveryProperties.getIp();
    }

    @Override
    public int getPort() {
        return redisDiscoveryProperties.getPort();
    }

    @Override
    public boolean isSecure() {
        return redisDiscoveryProperties.isSecure();
    }

    @Override
    public URI getUri() {
        return DefaultServiceInstance.getUri(this);
    }

    @Override
    public Map<String, String> getMetadata() {
        return Map.of();
    }

}

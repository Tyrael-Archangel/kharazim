package com.tyrael.kharazim.discovery.redis.discovery;

import com.tyrael.kharazim.discovery.redis.RedisServiceInstanceManager;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/3/7
 */
public class RedisDiscoveryClient implements DiscoveryClient {

    private final RedisServiceInstanceManager serviceInstanceManager;

    public RedisDiscoveryClient(RedisServiceInstanceManager serviceInstanceManager) {
        this.serviceInstanceManager = serviceInstanceManager;
    }

    @Override
    public String description() {
        return "Spring Cloud redis Discovery Client";
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        return new ArrayList<>(serviceInstanceManager.getInstances(serviceId));
    }

    @Override
    public List<String> getServices() {
        return new ArrayList<>(serviceInstanceManager.getServices());
    }

}

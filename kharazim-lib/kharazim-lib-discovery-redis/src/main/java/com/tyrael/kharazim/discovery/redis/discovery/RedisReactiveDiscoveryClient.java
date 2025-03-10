package com.tyrael.kharazim.discovery.redis.discovery;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import reactor.core.publisher.Flux;

/**
 * @author Tyrael Archangel
 * @since 2025/3/7
 */
public class RedisReactiveDiscoveryClient implements ReactiveDiscoveryClient {

    private final DiscoveryClient discoveryClient;

    public RedisReactiveDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public String description() {
        return "Spring Cloud redis Reactive Discovery Client";
    }

    @Override
    public Flux<ServiceInstance> getInstances(String serviceId) {
        return Flux.fromIterable(discoveryClient.getInstances(serviceId));
    }

    @Override
    public Flux<String> getServices() {
        return Flux.fromIterable(discoveryClient.getServices());
    }

}

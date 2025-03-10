package com.tyrael.kharazim.discovery.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyrael.kharazim.discovery.redis.discovery.RedisDiscoveryClient;
import com.tyrael.kharazim.discovery.redis.discovery.RedisReactiveDiscoveryClient;
import com.tyrael.kharazim.discovery.redis.registry.RedisAutoServiceRegistration;
import com.tyrael.kharazim.discovery.redis.registry.RedisRegistration;
import com.tyrael.kharazim.discovery.redis.registry.RedisServiceRegistry;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Tyrael Archangel
 * @since 2025/3/6
 */
public class RedisDiscoveryAutoConfiguration {

    @Bean
    public RedisDiscoveryProperties redisDiscoveryProperties(InetUtils inetUtils) {
        return new RedisDiscoveryProperties(inetUtils);
    }

    @Bean
    public RedisServiceInstanceManager redisServiceInstanceManager(RedisDiscoveryProperties redisDiscoveryProperties,
                                                                   ObjectMapper objectMapper,
                                                                   StringRedisTemplate redisTemplate) {
        return new RedisServiceInstanceManager(redisDiscoveryProperties, objectMapper, redisTemplate);
    }

    @Bean
    public DiscoveryClient discoveryClient(RedisServiceInstanceManager serviceInstanceManager) {
        return new RedisDiscoveryClient(serviceInstanceManager);
    }

    @Bean
    public RedisReactiveDiscoveryClient redisReactiveDiscoveryClient(DiscoveryClient discoveryClient) {
        return new RedisReactiveDiscoveryClient(discoveryClient);
    }

    @Bean
    public RedisRegistration redisRegistration(RedisDiscoveryProperties redisDiscoveryProperties) {
        return new RedisRegistration(redisDiscoveryProperties);
    }

    @Bean
    public RedisServiceRegistry redisServiceRegistry(RedisServiceInstanceManager serviceInstanceManager) {
        return new RedisServiceRegistry(serviceInstanceManager);
    }

    @Bean
    public RedisAutoServiceRegistration redisAutoServiceRegistration(RedisServiceRegistry redisServiceRegistry,
                                                                     RedisRegistration redisRegistration,
                                                                     AutoServiceRegistrationProperties properties) {
        return new RedisAutoServiceRegistration(redisServiceRegistry, redisRegistration, properties);
    }

}

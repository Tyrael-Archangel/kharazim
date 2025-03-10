package com.tyrael.kharazim.discovery.redis.registry;

import com.tyrael.kharazim.discovery.redis.RedisServiceInstance;
import com.tyrael.kharazim.discovery.redis.RedisServiceInstanceManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

/**
 * @author Tyrael Archangel
 * @since 2025/3/7
 */
@Slf4j
public class RedisServiceRegistry implements ServiceRegistry<RedisRegistration> {

    private final RedisServiceInstanceManager redisServiceInstanceManager;

    public RedisServiceRegistry(RedisServiceInstanceManager redisServiceInstanceManager) {
        this.redisServiceInstanceManager = redisServiceInstanceManager;
    }

    @Override
    public void register(RedisRegistration registration) {
        RedisServiceInstance serviceInstance = new RedisServiceInstance();
        serviceInstance.setInstanceId(registration.getInstanceId());
        serviceInstance.setServiceId(registration.getServiceId());
        serviceInstance.setHost(registration.getHost());
        serviceInstance.setPort(registration.getPort());
        serviceInstance.setUri(registration.getUri());
        serviceInstance.setSecure(registration.isSecure());

        redisServiceInstanceManager.heartBreakRegister(serviceInstance);
    }

    @Override
    public void deregister(RedisRegistration registration) {
        String serviceId = registration.getServiceId();
        String instanceId = registration.getInstanceId();
        redisServiceInstanceManager.deregister(serviceId, instanceId);
    }

    @Override
    public void setStatus(RedisRegistration registration, String status) {
        throw new IllegalStateException("not supported yet");
    }

    @Override
    public <T> T getStatus(RedisRegistration registration) {
        throw new IllegalStateException("not supported yet");
    }

    @Override
    public void close() {
        log.info("Closing Redis ServiceRegistry");
        redisServiceInstanceManager.close();
    }

}

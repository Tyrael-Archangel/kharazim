package com.tyrael.kharazim.discovery.redis.registry;

import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

/**
 * @author Tyrael Archangel
 * @since 2025/3/7
 */
public class RedisAutoServiceRegistration extends AbstractAutoServiceRegistration<RedisRegistration> {

    private final RedisRegistration redisRegistration;

    public RedisAutoServiceRegistration(ServiceRegistry<RedisRegistration> serviceRegistry,
                                        RedisRegistration redisRegistration,
                                        AutoServiceRegistrationProperties properties) {
        super(serviceRegistry, properties);
        this.redisRegistration = redisRegistration;
    }

    @Override
    protected Object getConfiguration() {
        return null;
    }

    @Override
    protected boolean isEnabled() {
        return true;
    }

    @Override
    protected RedisRegistration getRegistration() {
        return redisRegistration;
    }

    @Override
    protected RedisRegistration getManagementRegistration() {
        return null;
    }
}

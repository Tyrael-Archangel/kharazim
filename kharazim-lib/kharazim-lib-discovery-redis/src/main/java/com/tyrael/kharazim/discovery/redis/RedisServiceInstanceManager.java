package com.tyrael.kharazim.discovery.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/3/10
 */
public class RedisServiceInstanceManager {

    private final RedisDiscoveryProperties redisDiscoveryProperties;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final ScheduledExecutorService scheduledExecutorService;

    public RedisServiceInstanceManager(RedisDiscoveryProperties redisDiscoveryProperties,
                                       ObjectMapper objectMapper,
                                       StringRedisTemplate redisTemplate) {
        this.redisDiscoveryProperties = redisDiscoveryProperties;
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * heartBreakRegister
     */
    public void heartBreakRegister(RedisServiceInstance instance) {
        scheduledExecutorService.scheduleWithFixedDelay(
                () -> register(instance),
                0,
                (int) (redisDiscoveryProperties.getHealthDelaySeconds() * 0.7),
                TimeUnit.SECONDS);
    }

    /**
     * register
     */
    public void register(RedisServiceInstance instance) {

        String instanceKey = instanceKey(instance.getServiceId(), instance.getInstanceId());
        String instanceValue = serialize(instance);
        Duration expireTime = Duration.ofSeconds(redisDiscoveryProperties.getHealthDelaySeconds());

        redisTemplate.opsForValue().set(instanceKey, instanceValue, expireTime);
    }

    /**
     * deregister
     */
    public void deregister(String serviceId, String instanceId) {
        String instanceKey = instanceKey(serviceId, instanceId);
        redisTemplate.delete(instanceKey);
    }

    /**
     * getInstances
     */
    public List<RedisServiceInstance> getInstances(String serviceId) {
        ScanOptions instanceKeyScanOptions = ScanOptions.scanOptions()
                .match(this.instanceKey(serviceId, "*"))
                .build();
        try (Cursor<String> scanCursor = redisTemplate.scan(instanceKeyScanOptions)) {
            List<String> instanceKeys = scanCursor.stream().toList();
            if (instanceKeys.isEmpty()) {
                return List.of();
            }
            List<String> instanceValues = redisTemplate.opsForValue().multiGet(instanceKeys);
            if (instanceValues == null || instanceValues.isEmpty()) {
                return List.of();
            }
            return instanceValues.stream()
                    .map(this::deserialize)
                    .collect(Collectors.toList());
        }
    }

    /**
     * getServices
     */
    public List<String> getServices() {
        String serviceKeyPattern = instanceKey("*", "*");
        Set<String> instanceKeys = redisTemplate.keys(serviceKeyPattern);
        if (instanceKeys == null || instanceKeys.isEmpty()) {
            return List.of();
        }
        return instanceKeys.stream()
                .map(this::parseServiceId)
                .toList();
    }

    private String parseServiceId(String instanceKey) {
        String sep = redisDiscoveryProperties.getSep();
        try {
            return instanceKey.split(sep)[1];
        } catch (Exception e) {
            throw new IllegalStateException("Could not parse RedisServiceInstance: " + instanceKey);
        }
    }

    private String serialize(RedisServiceInstance redisServiceInstance) {
        if (redisServiceInstance == null) {
            return "";
        }
        try {
            return objectMapper.writeValueAsString(redisServiceInstance);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not serialize RedisServiceInstance", e);
        }
    }

    private RedisServiceInstance deserialize(String serializeString) {
        if (ObjectUtils.isEmpty(serializeString)) {
            return null;
        }
        try {
            return objectMapper.readValue(serializeString, RedisServiceInstance.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not de-serialize RedisServiceInstance", e);
        }
    }

    private String instanceKey(String serviceId, String instanceId) {
        String namespace = redisDiscoveryProperties.getNamespace();
        String sep = redisDiscoveryProperties.getSep();
        return namespace + sep + serviceId + sep + instanceId;
    }

    public void close() {
        scheduledExecutorService.shutdown();
    }

}

package com.tyrael.kharazim.discovery.redis;

import lombok.Data;
import org.springframework.cloud.client.ServiceInstance;

import java.net.URI;
import java.util.Map;

/**
 * @author Tyrael Archangel
 * @since 2025/3/7
 */
@Data
public class RedisServiceInstance implements ServiceInstance {

    private final Map<String, String> metadata = Map.of();

    private String instanceId;
    private String serviceId;
    private String host;
    private int port;
    private URI uri;
    private boolean secure;

}

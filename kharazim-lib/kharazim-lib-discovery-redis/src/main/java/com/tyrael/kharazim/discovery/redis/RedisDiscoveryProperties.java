package com.tyrael.kharazim.discovery.redis;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.util.ObjectUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.UUID;

/**
 * @author Tyrael Archangel
 * @since 2025/3/7
 */
@Data
@ConfigurationProperties("spring.cloud.redis.discovery")
public class RedisDiscoveryProperties {

    private final String instanceId = UUID.randomUUID().toString();
    private final InetUtils inetUtils;

    @Value("${spring.cloud.redis.discovery.service:${spring.application.name:}}")
    private String service;

    /**
     * namespace, separation registry of different environments.
     */
    private String namespace = "DISCOVERY_NAMESPACE:DEFAULT";

    /**
     * group name
     */
    private String group = "DEFAULT_GROUP";

    /**
     * The ip address your want to register for your service instance
     * needn't set it if the auto-detect ip works well.
     */
    private String ip;

    /**
     * which network interface's ip you want to register.
     */
    private String networkInterface = "";

    /**
     * The port your want to register for your service instance,
     * needn't set it if the auto-detect port works well.
     */
    @Value("${spring.cloud.redis.discovery.port:${server.port:}}")
    private int port;

    /**
     * whether your service is a https service.
     */
    private boolean secure = false;

    private long healthDelaySeconds = 20;

    private String sep = ":";

    public RedisDiscoveryProperties(InetUtils inetUtils) {
        this.inetUtils = inetUtils;
    }

    @PostConstruct
    public void init() throws Exception {
        if (ObjectUtils.isEmpty(ip)) {
            // traversing network interfaces if didn't specify an interface
            if (ObjectUtils.isEmpty(networkInterface)) {
                ip = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
            } else {
                NetworkInterface netInterface = NetworkInterface.getByName(networkInterface);
                if (netInterface == null) {
                    throw new IllegalArgumentException("no such interface " + networkInterface);
                }

                Enumeration<InetAddress> inetAddress = netInterface.getInetAddresses();
                while (inetAddress.hasMoreElements()) {
                    InetAddress currentAddress = inetAddress.nextElement();
                    if (!currentAddress.isLoopbackAddress()) {
                        ip = currentAddress.getHostAddress();
                        break;
                    }
                }
                if (ObjectUtils.isEmpty(ip)) {
                    throw new IllegalStateException("cannot find available ip from network interface " + networkInterface);
                }
            }
        }
    }

}

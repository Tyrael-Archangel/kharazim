package com.tyrael.kharazim.pos;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Tyrael Archangel
 * @since 2025/7/15
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class PosApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PosApiApplication.class, args);
    }

}

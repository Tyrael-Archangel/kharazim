package com.tyrael.kharazim.basicdata;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Tyrael Archangel
 * @since 2025/2/11
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class BasicDataApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicDataApiApplication.class, args);
    }

}

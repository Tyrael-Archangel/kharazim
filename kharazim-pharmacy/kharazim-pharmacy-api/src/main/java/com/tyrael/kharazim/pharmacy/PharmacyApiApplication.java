package com.tyrael.kharazim.pharmacy;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Tyrael Archangel
 * @since 2025/4/1
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class PharmacyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PharmacyApiApplication.class, args);
    }

}

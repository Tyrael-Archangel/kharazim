package com.tyrael.kharazim.purchase;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Tyrael Archangel
 * @since 2025/3/25
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class PurchaseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PurchaseApiApplication.class, args);
    }

}
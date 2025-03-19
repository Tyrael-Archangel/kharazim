package com.tyrael.kharazim.product;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Tyrael Archangel
 * @since 2025/3/19
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class ProductApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApiApplication.class, args);
    }

}

package com.tyrael.kharazim.user.api;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Tyrael Archangel
 * @since 2025/2/11
 */
@ComponentScan("com.tyrael.kharazim.user")
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
@MapperScan("com.tyrael.kharazim.user.app.mapper")
public class UserApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApiApplication.class, args);
    }

}

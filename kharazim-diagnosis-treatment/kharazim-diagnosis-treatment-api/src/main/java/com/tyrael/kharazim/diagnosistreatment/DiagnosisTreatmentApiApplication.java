package com.tyrael.kharazim.diagnosistreatment;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Tyrael Archangel
 * @since 2025/4/7
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class DiagnosisTreatmentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiagnosisTreatmentApiApplication.class, args);
    }

}

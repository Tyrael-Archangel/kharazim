package com.tyrael.kharazim.idgenerator;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Tyrael Archangel
 * @since 2025/2/18
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class IdGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IdGeneratorApplication.class);
        springApplication.run(args).registerShutdownHook();

        synchronized (IdGeneratorApplication.class) {
            try {
                IdGeneratorApplication.class.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}

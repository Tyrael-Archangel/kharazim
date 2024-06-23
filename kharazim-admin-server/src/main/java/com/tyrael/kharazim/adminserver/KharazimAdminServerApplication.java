package com.tyrael.kharazim.adminserver;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Tyrael Archangel
 * @since 2024/6/23
 */
@SpringBootApplication
@EnableAdminServer
public class KharazimAdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KharazimAdminServerApplication.class, args);
    }

}

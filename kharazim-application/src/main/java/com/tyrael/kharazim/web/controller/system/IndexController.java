package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.common.dto.Response;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
@Hidden
@RestController
public class IndexController {

    @Value("${spring.profiles.active:default}")
    private String profile;

    @RequestMapping("/")
    public Response index() {
        return this.bootstrap();
    }

    @RequestMapping("/bootstrap")
    public ProfileResponse bootstrap() {
        return new ProfileResponse(profile);
    }

    @Data
    public static class ProfileResponse extends Response {

        private final String profile;

        public ProfileResponse(String profile) {
            this.profile = profile;
            markSuccess();
        }
    }

}

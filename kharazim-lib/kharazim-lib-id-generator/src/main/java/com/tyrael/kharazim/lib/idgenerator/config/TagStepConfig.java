package com.tyrael.kharazim.lib.idgenerator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "id-generator")
public class TagStepConfig {

    private Integer defaultStep = 1;

    private Map<String, Integer> tagStep = new HashMap<>();

    public int getStep(String tag) {
        return tagStep.getOrDefault(tag, defaultStep);
    }

}

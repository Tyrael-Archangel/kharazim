package com.tyrael.kharazim.lib.web.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;

/**
 * @author Tyrael Archangel
 * @since 2025/2/11
 */
@AutoConfigureBefore(WebMvcCommonConfiguration.class)
public class DisableWebCommonConfiguration {
}

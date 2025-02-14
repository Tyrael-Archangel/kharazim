package com.tyrael.kharazim.user.api.sdk.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;

/**
 * @author Tyrael Archangel
 * @since 2025/2/11
 */
@AutoConfigureBefore(AuthUserResolveAutoConfiguration.class)
public class DisableAuthUserResolverConfiguration {
}

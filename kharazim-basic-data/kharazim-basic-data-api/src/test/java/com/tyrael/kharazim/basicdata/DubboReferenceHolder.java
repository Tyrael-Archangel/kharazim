package com.tyrael.kharazim.basicdata;

import com.tyrael.kharazim.user.sdk.model.AuthUser;
import com.tyrael.kharazim.user.sdk.service.UserServiceApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2025/3/5
 */
@Component
public class DubboReferenceHolder {

    @DubboReference
    public UserServiceApi userServiceApi;

    public AuthUser mockUser() {
        return userServiceApi.mock();
    }

}

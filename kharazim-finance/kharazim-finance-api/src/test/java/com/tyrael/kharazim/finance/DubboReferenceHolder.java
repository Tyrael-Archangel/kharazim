package com.tyrael.kharazim.finance;

import com.tyrael.kharazim.basicdata.sdk.service.CustomerServiceApi;
import com.tyrael.kharazim.basicdata.sdk.service.DictServiceApi;
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

    @DubboReference
    public DictServiceApi dictServiceApi;

    @DubboReference
    public CustomerServiceApi customerServiceApi;

    public AuthUser mockUser() {
        return userServiceApi.mock();
    }

}

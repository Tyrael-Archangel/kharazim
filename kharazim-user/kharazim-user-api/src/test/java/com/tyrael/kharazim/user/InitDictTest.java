package com.tyrael.kharazim.user;

import com.tyrael.kharazim.basicdata.sdk.service.DictServiceApi;
import com.tyrael.kharazim.user.app.constant.UserDictConstants;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
@SpringBootTest
public class InitDictTest {

    @Autowired
    private DictServiceApiHolder dictServiceApiHolder;

    @Test
    public void initDict() {
        DictServiceApi.Defaults defaults = new DictServiceApi.Defaults(dictServiceApiHolder.dictServiceApi);
        defaults.init(UserDictConstants.ENABLE_STATUS);
        defaults.init(UserDictConstants.USER_GENDER);
    }

}

@Component
class DictServiceApiHolder {

    @DubboReference
    DictServiceApi dictServiceApi;

}
package com.tyrael.kharazim.user;

import com.tyrael.kharazim.user.app.constant.UserDictConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
@SpringBootTest
public class InitDictTest {

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

    @Test
    public void initDict() {
        dubboReferenceHolder.dictServiceApi.init(UserDictConstants.ENABLE_STATUS);
        dubboReferenceHolder.dictServiceApi.init(UserDictConstants.USER_GENDER);
    }

}
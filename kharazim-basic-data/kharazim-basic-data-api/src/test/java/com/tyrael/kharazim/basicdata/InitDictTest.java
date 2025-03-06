package com.tyrael.kharazim.basicdata;

import com.tyrael.kharazim.basicdata.app.constant.BasicDataDictConstants;
import com.tyrael.kharazim.basicdata.sdk.service.DictServiceApi;
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
    private DictServiceApi dictServiceApi;

    @Test
    public void initDict() {
        dictServiceApi.init(BasicDataDictConstants.CUSTOMER_GENDER);
        dictServiceApi.init(BasicDataDictConstants.CUSTOMER_CERTIFICATE_TYPE);
        dictServiceApi.init(BasicDataDictConstants.SYSTEM_ADDRESS_LEVEL);
        dictServiceApi.init(BasicDataDictConstants.CLINIC_STATUS);

        dictServiceApi.init(BasicDataDictConstants.CUSTOMER_SOURCE_CHANNEL);
        dictServiceApi.init(BasicDataDictConstants.INSURANCE_COMPANY);
        dictServiceApi.init(BasicDataDictConstants.CUSTOMER_TAG);
        dictServiceApi.init(BasicDataDictConstants.COMMUNICATION_TYPE);
        dictServiceApi.init(BasicDataDictConstants.COMMUNICATION_EVALUATE);
    }

}
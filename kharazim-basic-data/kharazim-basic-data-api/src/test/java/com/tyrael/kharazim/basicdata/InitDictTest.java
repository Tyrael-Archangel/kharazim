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
        dictServiceApi.init(
                BasicDataDictConstants.CUSTOMER_GENDER,
                BasicDataDictConstants.CUSTOMER_CERTIFICATE_TYPE,
                BasicDataDictConstants.SYSTEM_ADDRESS_LEVEL,
                BasicDataDictConstants.CLINIC_STATUS,
                BasicDataDictConstants.CUSTOMER_SOURCE_CHANNEL,
                BasicDataDictConstants.INSURANCE_COMPANY,
                BasicDataDictConstants.CUSTOMER_TAG,
                BasicDataDictConstants.COMMUNICATION_TYPE,
                BasicDataDictConstants.COMMUNICATION_EVALUATE
        );
    }

}
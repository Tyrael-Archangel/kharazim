package com.tyrael.kharazim.basicdata;

import com.tyrael.kharazim.basicdata.app.constant.BasicDataDictConstants;
import com.tyrael.kharazim.basicdata.sdk.service.DictServiceApi;
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
        dictServiceApiHolder.dictServiceApi.init(BasicDataDictConstants.CUSTOMER_GENDER);
        dictServiceApiHolder.dictServiceApi.init(BasicDataDictConstants.CUSTOMER_CERTIFICATE_TYPE);

        dictServiceApiHolder.dictServiceApi.init(BasicDataDictConstants.CUSTOMER_SOURCE_CHANNEL);
        dictServiceApiHolder.dictServiceApi.init(BasicDataDictConstants.INSURANCE_COMPANY);
        dictServiceApiHolder.dictServiceApi.init(BasicDataDictConstants.CUSTOMER_TAG);
        dictServiceApiHolder.dictServiceApi.init(BasicDataDictConstants.COMMUNICATION_TYPE);
        dictServiceApiHolder.dictServiceApi.init(BasicDataDictConstants.COMMUNICATION_EVALUATE);
    }

}

@Component
class DictServiceApiHolder {

    @DubboReference
    DictServiceApi dictServiceApi;

}
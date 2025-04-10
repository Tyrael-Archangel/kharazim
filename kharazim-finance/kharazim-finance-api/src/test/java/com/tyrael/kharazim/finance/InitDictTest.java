package com.tyrael.kharazim.finance;

import com.tyrael.kharazim.finance.app.constant.FinanceDictConstants;
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
        dubboReferenceHolder.dictServiceApi.init(
                FinanceDictConstants.CUSTOMER_RECHARGE_STATUS,
                FinanceDictConstants.SETTLEMENT_ORDER_STATUS
        );
    }

}
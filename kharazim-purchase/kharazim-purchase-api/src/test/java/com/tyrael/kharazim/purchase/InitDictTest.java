package com.tyrael.kharazim.purchase;

import com.tyrael.kharazim.purchase.app.constant.PurchaseDictConstants;
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
        dubboReferenceHolder.dictServiceApi.init(PurchaseDictConstants.PURCHASE_RECEIVE_STATUS);
        dubboReferenceHolder.dictServiceApi.init(PurchaseDictConstants.PURCHASE_PAYMENT_STATUS);
    }

}
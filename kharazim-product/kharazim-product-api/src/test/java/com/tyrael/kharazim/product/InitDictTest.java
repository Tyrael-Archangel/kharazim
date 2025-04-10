package com.tyrael.kharazim.product;

import com.tyrael.kharazim.product.app.constant.ProductDictConstants;
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
                ProductDictConstants.SKU_PUBLISH_STATUS
        );
    }

}
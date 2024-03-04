package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
class ProductControllerTest extends BaseControllerTest<ProductController> {

    ProductControllerTest() {
        super(ProductController.class);
    }

    @Test
    void getByCode() {
        String code = "SPU";
        super.performWhenCall(mockController.getByCode(code));
    }

}
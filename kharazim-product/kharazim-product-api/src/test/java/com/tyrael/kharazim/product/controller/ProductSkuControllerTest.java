package com.tyrael.kharazim.product.controller;

import com.tyrael.kharazim.product.app.vo.sku.PageProductSkuRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
@Slf4j
class ProductSkuControllerTest extends BaseControllerTest<ProductSkuController> {

    ProductSkuControllerTest() {
        super(ProductSkuController.class);
    }

    @Test
    void getByCode() {
        String code = "P240311000001";
        super.performWhenCall(mockController.getByCode(code));
    }

    @Test
    void page() {
        PageProductSkuRequest pageRequest = new PageProductSkuRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

}
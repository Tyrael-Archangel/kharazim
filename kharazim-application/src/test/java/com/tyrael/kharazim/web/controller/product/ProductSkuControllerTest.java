package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.application.product.vo.sku.PageProductSkuRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
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
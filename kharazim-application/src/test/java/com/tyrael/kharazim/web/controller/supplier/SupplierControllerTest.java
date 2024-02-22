package com.tyrael.kharazim.web.controller.supplier;

import com.tyrael.kharazim.application.supplier.vo.PageSupplierRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/2/22
 */
class SupplierControllerTest extends BaseControllerTest<SupplierController> {

    SupplierControllerTest() {
        super(SupplierController.class);
    }

    @Test
    void page() {
        PageSupplierRequest pageRequest = new PageSupplierRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

}
package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
class AddressControllerTest extends BaseControllerTest<AddressController> {

    AddressControllerTest() {
        super(AddressController.class);
    }

    @Test
    void fullTree() {
        super.performWhenCall(mockController.fullTree());
    }

    @Test
    void treeByRootId() {
        super.performWhenCall(mockController.treeByRootId(2L));
    }

    @Test
    void queryByName() {
        super.performWhenCall(mockController.queryByName("北京市"));
    }

    @Test
    void listProvince() {
        super.performWhenCall(mockController.listProvince());
    }

    @Test
    void queryCities() {
        // 四川省
        super.performWhenCall(mockController.queryCities("22"));
    }

    @Test
    void queryCounties() {
        // 四川省-成都市
        super.performWhenCall(mockController.queryCounties("22-1"));
    }

}
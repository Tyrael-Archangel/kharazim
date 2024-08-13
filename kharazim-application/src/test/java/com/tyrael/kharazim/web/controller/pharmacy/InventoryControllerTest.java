package com.tyrael.kharazim.web.controller.pharmacy;

import com.tyrael.kharazim.application.pharmacy.vo.inventory.PageInventoryRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/8/13
 */
class InventoryControllerTest extends BaseControllerTest<InventoryController> {

    InventoryControllerTest() {
        super(InventoryController.class);
    }

    @Test
    void page() {
        PageInventoryRequest pageRequest = new PageInventoryRequest();
        this.performWhenCall(mockController.page(pageRequest));
    }

}

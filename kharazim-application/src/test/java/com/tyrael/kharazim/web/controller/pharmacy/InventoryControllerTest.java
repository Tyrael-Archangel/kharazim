package com.tyrael.kharazim.web.controller.pharmacy;

import com.tyrael.kharazim.application.pharmacy.vo.inventory.ListInventoryOfClinicRequest;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.PageInventoryRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

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

    @Test
    void listOfClinic() {
        ListInventoryOfClinicRequest listRequest = new ListInventoryOfClinicRequest();
        listRequest.setClinicCode("CL000002");
        listRequest.setSkuCodes(Set.of("P240808000509", "P240808000473"));
        this.performWhenCall(mockController.listOfClinic(listRequest));
    }

}

package com.tyrael.kharazim.purchase.controller;

import com.tyrael.kharazim.purchase.app.vo.purchaseorder.PagePurchaseOrderRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/8/9
 */
class PurchaseOrderControllerTest extends BaseControllerTest<PurchaseOrderController> {

    PurchaseOrderControllerTest() {
        super(PurchaseOrderController.class);
    }

    @Test
    void page() {
        PagePurchaseOrderRequest pageRequest = new PagePurchaseOrderRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

    @Test
    void detail() {
        String purchaseOrderCode = "";
        super.performWhenCall(mockController.detail(purchaseOrderCode));
    }

}
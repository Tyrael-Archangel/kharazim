package com.tyrael.kharazim.web.controller.pharmacy;

import com.tyrael.kharazim.application.pharmacy.vo.outboundorder.PageOutboundOrderRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
class OutboundOrderControllerTest extends BaseControllerTest<OutboundOrderController> {

    OutboundOrderControllerTest() {
        super(OutboundOrderController.class);
    }

    @Test
    void page() {
        PageOutboundOrderRequest pageRequest = new PageOutboundOrderRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

}
package com.tyrael.kharazim.web.controller.pharmacy;

import com.tyrael.kharazim.application.pharmacy.vo.inboundorder.AddInboundRequest;
import com.tyrael.kharazim.application.pharmacy.vo.inboundorder.PageInboundOrderRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/8/13
 */
class InboundOrderControllerTest extends BaseControllerTest<InboundOrderController> {

    InboundOrderControllerTest() {
        super(InboundOrderController.class);
    }

    @Test
    void page() {
        PageInboundOrderRequest pageRequest = new PageInboundOrderRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

    @Test
    void inbound() {
        AddInboundRequest inboundRequest = new AddInboundRequest();
        super.performWhenCall(mockController.inbound(inboundRequest, super.mockAdmin()));
    }

}

package com.tyrael.kharazim.web.controller.pharmacy;

import com.tyrael.kharazim.application.pharmacy.enums.OutboundOrderStatus;
import com.tyrael.kharazim.application.pharmacy.service.OutboundOrderService;
import com.tyrael.kharazim.application.pharmacy.vo.outboundorder.OutboundOrderVO;
import com.tyrael.kharazim.application.pharmacy.vo.outboundorder.OutboundRequest;
import com.tyrael.kharazim.application.pharmacy.vo.outboundorder.PageOutboundOrderRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
public class OutboundTest extends BaseControllerTest<OutboundOrderController> {

    @Autowired
    private OutboundOrderService outboundOrderService;

    OutboundTest() {
        super(OutboundOrderController.class);
    }

    @Test
    void outbound() {

        PageOutboundOrderRequest pageRequest = new PageOutboundOrderRequest();
        int pageIndex = 1;

        PageResponse<OutboundOrderVO> pageResponse;
        do {
            pageRequest.setPageIndex(pageIndex++);
            pageResponse = outboundOrderService.page(pageRequest);

            for (OutboundOrderVO outboundOrder : pageResponse.getData()) {
                if (OutboundOrderStatus.WAIT_OUTBOUND.equals(outboundOrder.getStatus())
                        && random.nextInt(100) > 20) {
                    OutboundRequest outboundRequest = new OutboundRequest();
                    outboundRequest.setOutboundOrderCode(outboundOrder.getCode());
                    super.performWhenCall(mockController.outbound(outboundRequest, super.mockUser()));
                }
            }

        } while (pageResponse.isSuccess() && pageResponse.getCurrentPageCount() > 0);
    }
}
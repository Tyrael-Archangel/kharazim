package com.tyrael.kharazim.web.controller.pharmacy;

import com.tyrael.kharazim.application.pharmacy.enums.InboundOrderStatus;
import com.tyrael.kharazim.application.pharmacy.service.InboundOrderService;
import com.tyrael.kharazim.application.pharmacy.vo.inboundorder.AddInboundRequest;
import com.tyrael.kharazim.application.pharmacy.vo.inboundorder.InboundOrderVO;
import com.tyrael.kharazim.application.pharmacy.vo.inboundorder.PageInboundOrderRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.util.CollectionUtils;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/8/13
 */
class InboundOrderControllerTest extends BaseControllerTest<InboundOrderController> {

    @Autowired
    private InboundOrderService inboundOrderService;

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
        PageInboundOrderRequest pageRequest = new PageInboundOrderRequest();
        int pageIndex = 1;

        PageResponse<InboundOrderVO> pageResponse;
        do {
            pageRequest.setPageIndex(pageIndex++);
            pageResponse = inboundOrderService.page(pageRequest);

            List<InboundOrderVO> waitInboundOrders = pageResponse.getData()
                    .stream()
                    .filter(e -> InboundOrderStatus.WAIT_INBOUND.equals(e.getStatus()))
                    .toList();

            for (InboundOrderVO inboundOrder : waitInboundOrders) {

                List<AddInboundRequest.InboundItem> inboundItems = null;

                int randomValue = random.nextInt(100);
                // 25%部分入库，60%全部入库，15%不入库
                if (randomValue <= 25) {
                    inboundItems = inboundOrder.getItems()
                            .stream()
                            .map(item -> new AddInboundRequest.InboundItem(
                                    item.getSkuCode(),
                                    random.nextInt(item.getRemainQuantity() + 1)))
                            .collect(Collectors.toList());

                } else if (randomValue > 40) {
                    inboundItems = inboundOrder.getItems()
                            .stream()
                            .map(item -> new AddInboundRequest.InboundItem(item.getSkuCode(), item.getRemainQuantity()))
                            .collect(Collectors.toList());
                }

                inboundItems = CollectionUtils.safeStream(inboundItems)
                        .filter(e -> e.getQuantity() > 0)
                        .collect(Collectors.toList());
                if (!inboundItems.isEmpty()) {
                    AddInboundRequest inboundRequest = new AddInboundRequest();
                    inboundRequest.setInboundOrderCode(inboundOrder.getCode());
                    inboundRequest.setItems(inboundItems);
                    super.performWhenCall(mockController.inbound(inboundRequest, super.mockUser()));
                }
            }

        } while (pageResponse.isSuccess() && pageResponse.getCurrentPageCount() > 0);
    }

}

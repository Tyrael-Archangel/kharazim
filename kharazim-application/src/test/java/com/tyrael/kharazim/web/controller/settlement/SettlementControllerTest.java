package com.tyrael.kharazim.web.controller.settlement;

import com.tyrael.kharazim.application.settlement.vo.PageSettlementOrderRequest;
import com.tyrael.kharazim.application.settlement.vo.SettlementPayCommand;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
class SettlementControllerTest extends BaseControllerTest<SettlementController> {

    SettlementControllerTest() {
        super(SettlementController.class);
    }

    @Test
    void detail() {
        String code = "BO20240403000001";
        super.performWhenCall(mockController.detail(code));
    }

    @Test
    void page() {
        PageSettlementOrderRequest pageRequest = new PageSettlementOrderRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

    @Test
    void payWithRechargeCard() {
        SettlementPayCommand payCommand = new SettlementPayCommand();
        // TODO @Tyrael Archangel
        super.performWhenCall(mockController.payWithRechargeCard(payCommand, super.mockAdmin()));
    }

}
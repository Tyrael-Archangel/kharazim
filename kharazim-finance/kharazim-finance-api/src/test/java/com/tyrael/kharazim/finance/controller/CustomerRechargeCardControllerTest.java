package com.tyrael.kharazim.finance.controller;

import com.tyrael.kharazim.finance.DubboReferenceHolder;
import com.tyrael.kharazim.finance.app.vo.recharge.CustomerRechargeCardChargebackRequest;
import com.tyrael.kharazim.finance.app.vo.recharge.CustomerRechargeCardPageRequest;
import com.tyrael.kharazim.finance.app.vo.recharge.PageCustomerRechargeCardLogRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2024/2/1
 */
@SpringBootTest
class CustomerRechargeCardControllerTest extends BaseControllerTest<CustomerRechargeCardController> {

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

    CustomerRechargeCardControllerTest() {
        super(CustomerRechargeCardController.class);
    }

    @Test
    @Rollback
    @Transactional(rollbackFor = Exception.class)
    void chargeback() {
        CustomerRechargeCardChargebackRequest chargebackRequest = new CustomerRechargeCardChargebackRequest();
        chargebackRequest.setRechargeCardCode("CRC20240201000001");
        chargebackRequest.setChargebackAmount(BigDecimal.valueOf(100));
        super.performWhenCall(mockController.chargeback(chargebackRequest, dubboReferenceHolder.mockUser()));
    }

    @Test
    void rechargeCardPage() {
        CustomerRechargeCardPageRequest pageRequest = new CustomerRechargeCardPageRequest();
        super.performWhenCall(mockController.rechargeCardPage(pageRequest));
    }

    @Test
    void listCustomerEffective() {
        String customerCode = "CU0000000002";
        super.performWhenCall(mockController.listCustomerEffective(customerCode));
    }

    @Test
    void pageRechargeCardLog() {
        String rechargeCardCode = "CRC20240201000001";
        PageCustomerRechargeCardLogRequest pageCommand = new PageCustomerRechargeCardLogRequest();
        super.performWhenCall(mockController.pageRechargeCardLog(rechargeCardCode, pageCommand));
    }

    @Test
    void markRefunded() {
        String code = "CRC20240201000001";
        super.performWhenCall(mockController.markRefunded(code, dubboReferenceHolder.mockUser()));
    }

    @Test
    void balanceOverview() {
        String customerCode = "CU0000000002";
        super.performWhenCall(mockController.balanceOverview(customerCode));
    }

    @Test
    void rechargeCardBalance() {
        String customerCode = "CU0000000002";
        super.performWhenCall(mockController.rechargeCardBalance(customerCode));
    }

}

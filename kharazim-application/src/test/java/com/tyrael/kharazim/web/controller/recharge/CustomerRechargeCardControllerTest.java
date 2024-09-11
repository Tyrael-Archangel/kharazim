package com.tyrael.kharazim.web.controller.recharge;

import com.tyrael.kharazim.application.customer.service.CustomerService;
import com.tyrael.kharazim.application.customer.vo.customer.CustomerSimpleVO;
import com.tyrael.kharazim.application.customer.vo.customer.ListCustomerRequest;
import com.tyrael.kharazim.application.recharge.enums.CustomerRechargeCardStatus;
import com.tyrael.kharazim.application.recharge.service.CustomerRechargeCardService;
import com.tyrael.kharazim.application.recharge.service.RechargeCardTypeService;
import com.tyrael.kharazim.application.recharge.vo.*;
import com.tyrael.kharazim.application.user.dto.user.request.ListUserRequest;
import com.tyrael.kharazim.application.user.dto.user.response.UserDTO;
import com.tyrael.kharazim.application.user.service.UserService;
import com.tyrael.kharazim.common.dto.PageCommand;
import com.tyrael.kharazim.common.util.CollectionUtils;
import com.tyrael.kharazim.mock.MockRandomPoetry;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/2/1
 */
class CustomerRechargeCardControllerTest extends BaseControllerTest<CustomerRechargeCardController> {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RechargeCardTypeService rechargeCardTypeService;

    @Autowired
    private CustomerRechargeCardService customerRechargeCardService;

    CustomerRechargeCardControllerTest() {
        super(CustomerRechargeCardController.class);
    }

    @Test
    void recharge() {
        List<UserDTO> users = userService.list(new ListUserRequest());
        List<CustomerSimpleVO> customers = customerService.listSimpleInfo(new ListCustomerRequest());
        List<RechargeCardTypeVO> cardTypes = rechargeCardTypeService.list(new ListRechargeCardTypeRequest());

        int totalCount = random.nextInt(500) + 200;
        for (int i = 0; i < totalCount; i++) {
            CustomerSimpleVO customer = CollectionUtils.random(customers);
            UserDTO user = CollectionUtils.random(users);
            RechargeCardTypeVO cardType = CollectionUtils.random(cardTypes);
            LocalDate rechargeDate = LocalDate.now().minusDays(random.nextInt(100));

            int defaultAmount = cardType.getDefaultAmount().intValue();
            int amount = random.nextInt(100) > 20
                    ? defaultAmount + random.nextInt(defaultAmount)
                    : defaultAmount - random.nextInt(defaultAmount);
            amount = amount / 100 * 100;
            amount = Math.max(amount, 100);

            CustomerRechargeRequest rechargeRequest = new CustomerRechargeRequest();
            rechargeRequest.setCustomerCode(customer.getCode());
            rechargeRequest.setCardTypeCode(cardType.getCode());
            rechargeRequest.setRechargeDate(rechargeDate);
            rechargeRequest.setAmount(BigDecimal.valueOf(amount));
            rechargeRequest.setTraderUserCode(user.getCode());
            rechargeRequest.setRemark(MockRandomPoetry.random());
            super.performWhenCall(mockController.recharge(rechargeRequest));
        }
    }

    @Test
    void markPaid() {
        int pageNum = 1;
        CustomerRechargeCardPageRequest request = new CustomerRechargeCardPageRequest();
        request.setPageSize(PageCommand.MAX_PAGE_SIZE);
        Collection<CustomerRechargeCardVO> customerRechargeCards;
        do {
            request.setPageNum(pageNum++);
            customerRechargeCards = customerRechargeCardService.page(request).getData();
            for (CustomerRechargeCardVO customerRechargeCard : customerRechargeCards) {
                if (random.nextInt(100) > 15
                        && CustomerRechargeCardStatus.UNPAID.equals(customerRechargeCard.getStatus())) {
                    super.performWhenCall(mockController.markPaid(customerRechargeCard.getCode(), super.mockAdmin()));
                }
            }
        } while (!customerRechargeCards.isEmpty());
    }

    @Test
    @Rollback
    @Transactional(rollbackFor = Exception.class)
    void chargeback() {
        CustomerRechargeCardChargebackRequest chargebackRequest = new CustomerRechargeCardChargebackRequest();
        chargebackRequest.setRechargeCardCode("CRC20240201000001");
        chargebackRequest.setChargebackAmount(BigDecimal.valueOf(100));
        super.performWhenCall(mockController.chargeback(chargebackRequest, super.mockAdmin()));
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
        super.performWhenCall(mockController.markRefunded(code, super.mockAdmin()));
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

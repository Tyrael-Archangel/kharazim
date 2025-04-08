package com.tyrael.kharazim.finance.controller;

import com.tyrael.kharazim.authentication.PrincipalHolder;
import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.sdk.model.CustomerVO;
import com.tyrael.kharazim.finance.DubboReferenceHolder;
import com.tyrael.kharazim.finance.app.enums.CustomerRechargeCardStatus;
import com.tyrael.kharazim.finance.app.service.CustomerRechargeCardService;
import com.tyrael.kharazim.finance.app.service.RechargeCardTypeService;
import com.tyrael.kharazim.finance.app.vo.recharge.*;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import com.tyrael.kharazim.test.mock.MockRandomPoetry;
import com.tyrael.kharazim.user.sdk.model.UserSimpleVO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/2/1
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddCustomerRechargeCardTest extends BaseControllerTest<CustomerRechargeCardController> {

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

    @Autowired
    private RechargeCardTypeService rechargeCardTypeService;

    @Autowired
    private CustomerRechargeCardService customerRechargeCardService;

    public AddCustomerRechargeCardTest() {
        super(CustomerRechargeCardController.class);
    }

    @Test
    @Order(1)
    void recharge() {
        List<UserSimpleVO> users = dubboReferenceHolder.userServiceApi.listAll();
        List<CustomerVO> customers = dubboReferenceHolder.customerServiceApi.listAll();
        List<RechargeCardTypeVO> cardTypes = rechargeCardTypeService.list(new ListRechargeCardTypeRequest());

        int totalCount = random.nextInt(500) + 200;
        for (int i = 0; i < totalCount; i++) {
            PrincipalHolder.setPrincipal(dubboReferenceHolder.mockUser());
            CustomerVO customer = CollectionUtils.random(customers);
            UserSimpleVO user = CollectionUtils.random(users);
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
    @Order(2)
    void markPaid() {
        int pageNum = 1;
        CustomerRechargeCardPageRequest request = new CustomerRechargeCardPageRequest();
        request.setPageSize(PageCommand.MAX_PAGE_SIZE);
        Collection<CustomerRechargeCardVO> customerRechargeCards;
        do {
            request.setPageIndex(pageNum++);
            customerRechargeCards = customerRechargeCardService.page(request).getData();
            for (CustomerRechargeCardVO customerRechargeCard : customerRechargeCards) {
                if (random.nextInt(100) > 15
                        && CustomerRechargeCardStatus.UNPAID.equals(customerRechargeCard.getStatus())) {
                    super.performWhenCall(mockController.markPaid(customerRechargeCard.getCode(), dubboReferenceHolder.mockUser()));
                }
            }
        } while (!customerRechargeCards.isEmpty());
    }

}

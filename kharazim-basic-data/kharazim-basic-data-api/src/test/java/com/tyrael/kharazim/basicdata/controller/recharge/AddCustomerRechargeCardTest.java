package com.tyrael.kharazim.basicdata.controller.recharge;

import com.tyrael.kharazim.authentication.PrincipalHolder;
import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.BasicDataApiApplication;
import com.tyrael.kharazim.basicdata.DubboReferenceHolder;
import com.tyrael.kharazim.basicdata.app.dto.customer.customer.CustomerSimpleVO;
import com.tyrael.kharazim.basicdata.app.dto.customer.customer.ListCustomerRequest;
import com.tyrael.kharazim.basicdata.app.dto.recharge.*;
import com.tyrael.kharazim.basicdata.app.enums.CustomerRechargeCardStatus;
import com.tyrael.kharazim.basicdata.app.service.customer.CustomerService;
import com.tyrael.kharazim.basicdata.app.service.recharge.CustomerRechargeCardService;
import com.tyrael.kharazim.basicdata.app.service.recharge.RechargeCardTypeService;
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
@SpringBootTest(classes = BasicDataApiApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddCustomerRechargeCardTest extends BaseControllerTest<CustomerRechargeCardController> {

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

    @Autowired
    private CustomerService customerService;

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
        List<CustomerSimpleVO> customers = customerService.listSimpleInfo(new ListCustomerRequest());
        List<RechargeCardTypeVO> cardTypes = rechargeCardTypeService.list(new ListRechargeCardTypeRequest());

        int totalCount = random.nextInt(500) + 200;
        for (int i = 0; i < totalCount; i++) {
            PrincipalHolder.setPrincipal(dubboReferenceHolder.mockUser());
            CustomerSimpleVO customer = CollectionUtils.random(customers);
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

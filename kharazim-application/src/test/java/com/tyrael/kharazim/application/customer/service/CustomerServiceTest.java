package com.tyrael.kharazim.application.customer.service;

import com.tyrael.kharazim.application.customer.vo.customer.PageCustomerRequest;
import com.tyrael.kharazim.mock.MockFileHttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

/**
 * @author Tyrael Archangel
 * @since 2024/5/22
 */
@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Test
    void export() throws IOException {
        String fileName = "会员数据" + System.currentTimeMillis() + ".xlsx";
        customerService.export(new PageCustomerRequest(), new MockFileHttpServletResponse(fileName));
    }

}
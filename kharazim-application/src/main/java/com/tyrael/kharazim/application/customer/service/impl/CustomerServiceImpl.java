package com.tyrael.kharazim.application.customer.service.impl;

import com.tyrael.kharazim.application.customer.converter.CustomerConverter;
import com.tyrael.kharazim.application.customer.domain.Customer;
import com.tyrael.kharazim.application.customer.mapper.CustomerMapper;
import com.tyrael.kharazim.application.customer.service.CustomerService;
import com.tyrael.kharazim.application.customer.vo.CustomerBaseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerConverter customerConverter;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerBaseVO findByCode(String code) {
        Customer customer = customerMapper.exactlyFindByCode(code);
        return customerConverter.customerBaseVO(customer);
    }
}

package com.tyrael.kharazim.basicdata.provider;

import com.tyrael.kharazim.basicdata.app.dto.customer.customer.CustomerBaseVO;
import com.tyrael.kharazim.basicdata.app.service.customer.CustomerService;
import com.tyrael.kharazim.basicdata.sdk.model.CustomerVO;
import com.tyrael.kharazim.basicdata.sdk.service.CustomerServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/4/7
 */
@Component
@DubboService
@RequiredArgsConstructor
public class CustomerServiceProvider implements CustomerServiceApi {

    private final CustomerService customerService;

    @Override
    public List<CustomerVO> listAll() {
        List<CustomerBaseVO> customers = customerService.listAll();
        return customers.stream()
                .map(e -> new CustomerVO().setCode(e.getCode()).setName(e.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerVO> listByCodes(Collection<String> codes) {
        List<CustomerBaseVO> customers = customerService.listByCodes(codes);
        return customers.stream()
                .map(e -> new CustomerVO().setCode(e.getCode()).setName(e.getName()))
                .collect(Collectors.toList());
    }

}

package com.tyrael.kharazim.basicdata.sdk.service;

import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.basicdata.sdk.model.CustomerVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/4/7
 */
public interface CustomerServiceApi {

    /**
     * find by code
     *
     * @param code code
     * @return CustomerVO
     */
    CustomerVO findByCode(String code);

    /**
     * exactly find by code
     *
     * @param code code
     * @return CustomerVO
     */
    default CustomerVO exactlyFindByCode(String code) {
        CustomerVO customer = findByCode(code);
        DomainNotFoundException.assertFound(customer, code);
        return customer;
    }

    /**
     * list all
     *
     * @return CustomerVOs
     */
    List<CustomerVO> listAll();

    /**
     * list by customer codes
     *
     * @param codes customer codes
     * @return CustomerVOs
     */
    List<CustomerVO> listByCodes(Collection<String> codes);

    /**
     * map by codes
     *
     * @param codes customer codes
     * @return code -> CustomerVO
     */
    default Map<String, CustomerVO> mapByCodes(Collection<String> codes) {
        List<CustomerVO> customers = listByCodes(codes);
        return customers.stream()
                .collect(Collectors.toMap(CustomerVO::getCode, e -> e));
    }

}

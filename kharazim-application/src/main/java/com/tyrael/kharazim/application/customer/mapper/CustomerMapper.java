package com.tyrael.kharazim.application.customer.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.customer.domain.Customer;
import com.tyrael.kharazim.application.customer.vo.ListCustomerRequest;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/7
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

    /**
     * find by code
     *
     * @param code code
     * @return Customer
     */
    default Customer findByCode(String code) {
        LambdaQueryWrapper<Customer> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Customer::getCode, code);
        return selectOne(queryWrapper);
    }

    /**
     * find by code exactly, if not exists throw DomainNotFoundException
     *
     * @param code code
     * @return Customer
     * @throws DomainNotFoundException if code not exists
     */
    default Customer exactlyFindByCode(String code) throws DomainNotFoundException {
        Customer customer = findByCode(code);
        DomainNotFoundException.assertFound(customer, code);
        return customer;
    }

    /**
     * 验证会员存在
     *
     * @param code 会员编码
     */
    default void ensureCustomerExist(String code) {
        LambdaQueryWrapper<Customer> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Customer::getCode, code);
        if (!this.exists(queryWrapper)) {
            throw new DomainNotFoundException("customerCode: " + code);
        }
    }

    /**
     * list
     *
     * @param request ListCustomerRequestVO
     * @return Customers
     */
    default List<Customer> list(ListCustomerRequest request) {
        LambdaQueryWrapper<Customer> queryWrapper = Wrappers.lambdaQuery();
        String keyword = StringUtils.trim(request.getKeyword());
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper = switch (request.getConditionType()) {
                case NAME -> queryWrapper.like(Customer::getName, keyword);
                case PHONE -> queryWrapper.eq(Customer::getPhone, keyword);
                case CERTIFICATE -> queryWrapper.eq(Customer::getCertificateCode, keyword);
            };
        }

        return selectList(queryWrapper);
    }

}

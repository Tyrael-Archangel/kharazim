package com.tyrael.kharazim.basicdata.app.mapper.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.app.domain.customer.Customer;
import com.tyrael.kharazim.basicdata.app.dto.customer.customer.ListCustomerRequest;
import com.tyrael.kharazim.basicdata.app.dto.customer.customer.PageCustomerRequest;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import com.tyrael.kharazim.mybatis.LambdaQueryWrapperX;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/1/7
 */
@Mapper
public interface CustomerMapper extends BasePageMapper<Customer> {

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
     * page
     *
     * @param pageRequest {@link PageCustomerRequest}
     * @return Customers
     */
    default PageResponse<Customer> page(PageCustomerRequest pageRequest, Page<Customer> pageCondition) {
        LambdaQueryWrapperX<Customer> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfHasText(Customer::getCode, pageRequest.getCode());
        queryWrapper.likeIfPresent(Customer::getName, pageRequest.getName());
        queryWrapper.eqIfHasText(Customer::getName, pageRequest.getPhone());
        queryWrapper.orderByAsc(Customer::getCode);

        return page(pageCondition, queryWrapper);
    }

    /**
     * list
     *
     * @param request ListCustomerRequestVO
     * @return Customers
     */
    default List<Customer> list(ListCustomerRequest request) {
        LambdaQueryWrapper<Customer> queryWrapper = Wrappers.lambdaQuery();
        String keyword = StringUtils.trim(request.getKeywords());
        if (StringUtils.isNotBlank(keyword)) {
            ListCustomerRequest.QueryConditionType conditionType = request.getConditionType();
            if (conditionType == null) {
                queryWrapper.and(q -> q.eq(Customer::getCode, keyword)
                        .or().like(Customer::getName, keyword)
                        .or().eq(Customer::getPhone, keyword)
                        .or().eq(Customer::getCertificateCode, keyword)
                );
            } else {
                queryWrapper = switch (conditionType) {
                    case CODE -> queryWrapper.eq(Customer::getCode, keyword);
                    case NAME -> queryWrapper.like(Customer::getName, keyword);
                    case PHONE -> queryWrapper.eq(Customer::getPhone, keyword);
                    case CERTIFICATE -> queryWrapper.eq(Customer::getCertificateCode, keyword);
                };
            }
        }

        return selectList(queryWrapper);
    }

    /**
     * list by codes
     *
     * @param codes codes
     * @return Customers
     */
    default List<Customer> listByCodes(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Customer> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Customer::getCode, codes);
        return selectList(queryWrapper);
    }

    /**
     * map by codes
     *
     * @param codes codes
     * @return Map<code, Customer>
     */
    default Map<String, Customer> mapByCodes(Collection<String> codes) {
        List<Customer> customers = listByCodes(codes);
        return customers.stream()
                .collect(Collectors.toMap(Customer::getCode, e -> e));
    }

}

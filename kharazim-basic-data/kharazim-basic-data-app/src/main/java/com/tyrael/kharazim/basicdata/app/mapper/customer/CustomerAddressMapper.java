package com.tyrael.kharazim.basicdata.app.mapper.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.basicdata.app.domain.customer.CustomerAddress;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/7
 */
@Mapper
public interface CustomerAddressMapper extends BaseMapper<CustomerAddress> {

    /**
     * list by customerCode
     *
     * @param code 会员编码
     * @return CustomerAddresses
     */
    default List<CustomerAddress> listByCustomerCode(String code) {
        LambdaQueryWrapper<CustomerAddress> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CustomerAddress::getCustomerCode, code);
        return selectList(queryWrapper);
    }

    /**
     * 设为默认地址
     *
     * @param customerCode      会员编码
     * @param customerAddressId 会员地址ID
     */
    default void markAddressDefault(String customerCode, Long customerAddressId) {
        LambdaUpdateWrapper<CustomerAddress> markAllNotDefaultWrapper = Wrappers.lambdaUpdate();
        markAllNotDefaultWrapper.eq(CustomerAddress::getCustomerCode, customerCode);
        markAllNotDefaultWrapper.eq(CustomerAddress::getDefaultAddress, true);
        markAllNotDefaultWrapper.set(CustomerAddress::getDefaultAddress, false);
        this.update(null, markAllNotDefaultWrapper);

        LambdaUpdateWrapper<CustomerAddress> markAddressDefaultWrapper = Wrappers.lambdaUpdate();
        markAddressDefaultWrapper.eq(CustomerAddress::getId, customerAddressId);
        markAddressDefaultWrapper.set(CustomerAddress::getDefaultAddress, true);
        this.update(null, markAddressDefaultWrapper);
    }

}

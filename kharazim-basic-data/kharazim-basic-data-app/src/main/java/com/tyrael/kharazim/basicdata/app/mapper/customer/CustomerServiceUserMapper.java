package com.tyrael.kharazim.basicdata.app.mapper.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.basicdata.app.domain.customer.CustomerServiceUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/1/9
 */
@Mapper
public interface CustomerServiceUserMapper extends BaseMapper<CustomerServiceUser> {

    /**
     * find by customerCode
     *
     * @param code 会员编码
     * @return CustomerServiceUser
     */
    default CustomerServiceUser findByCustomerCode(String code) {
        LambdaQueryWrapper<CustomerServiceUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CustomerServiceUser::getCustomerCode, code);
        return selectOne(queryWrapper);
    }

}


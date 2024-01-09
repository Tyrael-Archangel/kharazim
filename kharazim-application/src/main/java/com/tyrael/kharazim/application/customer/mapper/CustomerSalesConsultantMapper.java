package com.tyrael.kharazim.application.customer.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.customer.domain.CustomerSalesConsultant;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/1/9
 */
@Mapper
public interface CustomerSalesConsultantMapper extends BaseMapper<CustomerSalesConsultant> {

    /**
     * find by customerCode
     *
     * @param code 会员编码
     * @return CustomerSalesConsultant
     */
    default CustomerSalesConsultant findByCustomerCode(String code) {
        LambdaQueryWrapper<CustomerSalesConsultant> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CustomerSalesConsultant::getCustomerCode, code);
        return selectOne(queryWrapper);
    }

}

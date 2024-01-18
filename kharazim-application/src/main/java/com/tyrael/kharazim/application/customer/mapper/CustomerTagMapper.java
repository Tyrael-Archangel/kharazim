package com.tyrael.kharazim.application.customer.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.customer.domain.CustomerTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/18
 */
@Mapper
public interface CustomerTagMapper extends BaseMapper<CustomerTag> {

    /**
     * list by
     *
     * @param customerCode 会员编码
     * @return CustomerTags
     */
    default List<CustomerTag> listByCustomerCode(String customerCode) {
        LambdaQueryWrapper<CustomerTag> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CustomerTag::getCustomerCode, customerCode);
        return selectList(queryWrapper);
    }

}

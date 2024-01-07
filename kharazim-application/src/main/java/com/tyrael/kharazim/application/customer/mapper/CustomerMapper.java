package com.tyrael.kharazim.application.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyrael.kharazim.application.customer.domain.Customer;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/1/7
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}

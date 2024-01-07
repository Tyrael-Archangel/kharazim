package com.tyrael.kharazim.application.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyrael.kharazim.application.customer.domain.CustomerAddress;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/1/7
 */
@Mapper
public interface CustomerAddressMapper extends BaseMapper<CustomerAddress> {
}

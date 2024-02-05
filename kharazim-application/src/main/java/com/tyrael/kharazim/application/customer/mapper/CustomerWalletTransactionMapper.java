package com.tyrael.kharazim.application.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyrael.kharazim.application.customer.domain.CustomerWalletTransaction;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/2/5
 */
@Mapper
public interface CustomerWalletTransactionMapper extends BaseMapper<CustomerWalletTransaction> {
}

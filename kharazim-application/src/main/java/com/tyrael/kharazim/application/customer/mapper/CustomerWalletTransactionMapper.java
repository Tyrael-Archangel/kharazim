package com.tyrael.kharazim.application.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.customer.domain.CustomerWalletTransaction;
import com.tyrael.kharazim.application.customer.enums.TransactionTypeEnum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/2/5
 */
@Mapper
public interface CustomerWalletTransactionMapper extends BaseMapper<CustomerWalletTransaction> {

    /**
     * 按类型查询会员的交易记录
     *
     * @param customerCode 会员编码
     * @return CustomerWalletTransactions
     */
    default List<CustomerWalletTransaction> listByType(String customerCode,
                                                       TransactionTypeEnum type) {
        LambdaQueryWrapperX<CustomerWalletTransaction> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eq(CustomerWalletTransaction::getCustomerCode, customerCode)
                .eq(CustomerWalletTransaction::getType, type);
        return selectList(queryWrapper);
    }


}

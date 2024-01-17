package com.tyrael.kharazim.application.customer.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.customer.domain.CustomerInsurance;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/10
 */
@Mapper
public interface CustomerInsuranceMapper extends BaseMapper<CustomerInsurance> {

    /**
     * list by customerCode
     *
     * @param code 会员编码
     * @return CustomerInsurances
     */
    default List<CustomerInsurance> listByCustomerCode(String code) {
        LambdaQueryWrapper<CustomerInsurance> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CustomerInsurance::getCustomerCode, code);
        return selectList(queryWrapper);
    }

    /**
     * 设为默认保险
     *
     * @param customerCode        会员编码
     * @param customerInsuranceId 会员保险ID
     */
    default void markInsuranceDefault(String customerCode, Long customerInsuranceId) {
        LambdaUpdateWrapper<CustomerInsurance> markAllNotDefaultWrapper = Wrappers.lambdaUpdate();
        markAllNotDefaultWrapper.eq(CustomerInsurance::getCustomerCode, customerCode);
        markAllNotDefaultWrapper.eq(CustomerInsurance::getDefaultInsurance, true);
        markAllNotDefaultWrapper.set(CustomerInsurance::getDefaultInsurance, false);
        this.update(null, markAllNotDefaultWrapper);

        LambdaUpdateWrapper<CustomerInsurance> markAddressDefaultWrapper = Wrappers.lambdaUpdate();
        markAddressDefaultWrapper.eq(CustomerInsurance::getId, customerInsuranceId);
        markAddressDefaultWrapper.set(CustomerInsurance::getDefaultInsurance, true);
        this.update(null, markAddressDefaultWrapper);
    }

    /**
     * 删除
     *
     * @param id 会员保险ID
     */
    default void deleteCustomerInsurance(Long id) {
        LambdaUpdateWrapper<CustomerInsurance> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(CustomerInsurance::getId, id)
                .set(CustomerInsurance::getDeleted, Boolean.TRUE)
                .set(CustomerInsurance::getDeletedTimestamp, System.currentTimeMillis());
        this.update(null, updateWrapper);
    }

}

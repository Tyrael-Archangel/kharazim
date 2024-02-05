package com.tyrael.kharazim.application.recharge.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.recharge.domain.CustomerRechargeCardLog;
import com.tyrael.kharazim.application.recharge.vo.PageCustomerRechargeCardLogRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/2/5
 */
@Mapper
public interface CustomerRechargeCardLogMapper extends BaseMapper<CustomerRechargeCardLog> {

    /**
     * page
     *
     * @param rechargeCardCode 储值单号
     * @param pageCommand      分页条件
     * @return 储值单日志分页数据
     */
    default PageResponse<CustomerRechargeCardLog> page(String rechargeCardCode,
                                                       PageCustomerRechargeCardLogRequest pageCommand) {
        LambdaQueryWrapper<CustomerRechargeCardLog> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CustomerRechargeCardLog::getRechargeCardCode, rechargeCardCode);
        Page<CustomerRechargeCardLog> page = new Page<>(pageCommand.getPageNum(), pageCommand.getPageSize());
        Page<CustomerRechargeCardLog> pageData = this.selectPage(page, queryWrapper);
        return PageResponse.success(pageData.getRecords(),
                pageData.getTotal(),
                pageCommand.getPageSize(),
                pageCommand.getPageNum());
    }

}

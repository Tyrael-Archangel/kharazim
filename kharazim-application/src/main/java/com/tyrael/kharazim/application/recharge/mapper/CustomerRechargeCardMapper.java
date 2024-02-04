package com.tyrael.kharazim.application.recharge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.recharge.domain.CustomerRechargeCard;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeCardPageRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/2/1
 */
@Mapper
public interface CustomerRechargeCardMapper extends BaseMapper<CustomerRechargeCard> {

    /**
     * page
     *
     * @param pageRequest CustomerRechargeCardPageRequest
     * @return pageResponse
     */
    default PageResponse<CustomerRechargeCard> page(CustomerRechargeCardPageRequest pageRequest) {
        LambdaQueryWrapperX<CustomerRechargeCard> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfHasText(CustomerRechargeCard::getCode, pageRequest.getCode())
                .eqIfHasText(CustomerRechargeCard::getCustomerCode, pageRequest.getCustomerCode())
                .eqIfHasText(CustomerRechargeCard::getTraderUserCode, pageRequest.getTraderUserCode())
                .geIfPresent(CustomerRechargeCard::getRechargeDate, pageRequest.getStartDate())
                .leIfPresent(CustomerRechargeCard::getRechargeDate, pageRequest.getEndDate());

        Page<CustomerRechargeCard> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<CustomerRechargeCard> pageResponse = selectPage(page, queryWrapper);
        return PageResponse.success(pageResponse.getRecords(),
                pageResponse.getTotal(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }

}

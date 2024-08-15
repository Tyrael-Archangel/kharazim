package com.tyrael.kharazim.application.pharmacy.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.pharmacy.domain.OutboundOrder;
import com.tyrael.kharazim.application.pharmacy.vo.outboundorder.PageOutboundOrderRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@Mapper
public interface OutboundOrderMapper extends BaseMapper<OutboundOrder> {

    /**
     * page
     *
     * @param pageRequest {@link PageOutboundOrderRequest}
     * @return page data
     */
    default PageResponse<OutboundOrder> page(PageOutboundOrderRequest pageRequest) {
        LambdaQueryWrapperX<OutboundOrder> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfHasText(OutboundOrder::getCode, pageRequest.getCode());
        queryWrapper.eqIfHasText(OutboundOrder::getSourceBusinessCode, pageRequest.getSourceBusinessCode());
        queryWrapper.eqIfPresent(OutboundOrder::getStatus, pageRequest.getStatus());
        queryWrapper.inIfPresent(OutboundOrder::getClinicCode, pageRequest.getClinicCodes());
        queryWrapper.eqIfHasText(OutboundOrder::getCustomerCode, pageRequest.getCustomerCode());

        queryWrapper.orderByDesc(OutboundOrder::getCode);

        Page<OutboundOrder> pageCondition = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<OutboundOrder> pageData = selectPage(pageCondition, queryWrapper);
        return PageResponse.success(
                pageData.getRecords(),
                pageData.getTotal(),
                (int) pageCondition.getSize(),
                (int) pageCondition.getCurrent());
    }

    /**
     * find by code
     *
     * @param code OutboundOrder.code
     * @return OutboundOrder
     */
    default OutboundOrder findByCode(String code) {
        LambdaQueryWrapper<OutboundOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OutboundOrder::getCode, code);
        return selectOne(queryWrapper);
    }

}

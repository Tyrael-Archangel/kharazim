package com.tyrael.kharazim.pharmacy.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import com.tyrael.kharazim.mybatis.LambdaQueryWrapperX;
import com.tyrael.kharazim.pharmacy.app.domain.OutboundOrder;
import com.tyrael.kharazim.pharmacy.app.vo.outbound.PageOutboundOrderRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@Mapper
public interface OutboundOrderMapper extends BasePageMapper<OutboundOrder> {

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

        return page(pageRequest, queryWrapper);
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

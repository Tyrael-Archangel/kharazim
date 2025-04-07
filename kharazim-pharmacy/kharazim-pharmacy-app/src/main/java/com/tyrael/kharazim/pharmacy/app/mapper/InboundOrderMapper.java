package com.tyrael.kharazim.pharmacy.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import com.tyrael.kharazim.mybatis.LambdaQueryWrapperX;
import com.tyrael.kharazim.pharmacy.app.domain.InboundOrder;
import com.tyrael.kharazim.pharmacy.app.vo.inbound.PageInboundOrderRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/7/12
 */
@Mapper
public interface InboundOrderMapper extends BasePageMapper<InboundOrder> {

    /**
     * find by code FOR UPDATE
     *
     * @param code InboundOrder.code
     * @return InboundOrder
     */
    default InboundOrder findByCodeForUpdate(String code) {
        LambdaQueryWrapper<InboundOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InboundOrder::getCode, code);
        queryWrapper.last(" FOR UPDATE");
        return selectOne(queryWrapper);
    }

    /**
     * page
     *
     * @param pageRequest {@link PageInboundOrderRequest}
     * @return page data
     */
    default PageResponse<InboundOrder> page(PageInboundOrderRequest pageRequest) {
        LambdaQueryWrapperX<InboundOrder> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfHasText(InboundOrder::getCode, pageRequest.getCode());
        queryWrapper.eqIfHasText(InboundOrder::getSourceBusinessCode, pageRequest.getSourceBusinessCode());
        queryWrapper.inIfPresent(InboundOrder::getClinicCode, pageRequest.getClinicCodes());
        queryWrapper.inIfPresent(InboundOrder::getSupplierCode, pageRequest.getSupplierCodes());
        queryWrapper.eqIfPresent(InboundOrder::getStatus, pageRequest.getStatus());

        queryWrapper.orderByDesc(InboundOrder::getCode);

        return page(pageRequest, queryWrapper);
    }

}

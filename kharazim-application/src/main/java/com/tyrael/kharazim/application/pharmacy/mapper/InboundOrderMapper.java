package com.tyrael.kharazim.application.pharmacy.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.pharmacy.domain.InboundOrder;
import com.tyrael.kharazim.application.pharmacy.vo.inboundorder.PageInboundOrderRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/7/12
 */
@Mapper
public interface InboundOrderMapper extends BaseMapper<InboundOrder> {

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

        Page<InboundOrder> pageCondition = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<InboundOrder> pageData = selectPage(pageCondition, queryWrapper);
        return PageResponse.success(
                pageData.getRecords(),
                pageData.getTotal(),
                (int) pageCondition.getSize(),
                (int) pageCondition.getCurrent());
    }

}

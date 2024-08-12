package com.tyrael.kharazim.application.purchase.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrder;
import com.tyrael.kharazim.application.purchase.vo.request.PagePurchaseOrderRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.util.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Mapper
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {
    /**
     * find by code
     *
     * @param code PurchaseOrder.code
     * @return PurchaseOrder
     */
    default PurchaseOrder findByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        LambdaQueryWrapper<PurchaseOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchaseOrder::getCode, code);
        return selectOne(queryWrapper);
    }

    /**
     * 采购单分页
     *
     * @param pageRequest {@link PagePurchaseOrderRequest}
     * @return 采购单分页数据
     */
    default PageResponse<PurchaseOrder> page(PagePurchaseOrderRequest pageRequest) {
        LambdaQueryWrapperX<PurchaseOrder> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfHasText(PurchaseOrder::getCode, pageRequest.getPurchaseOrderCode());
        queryWrapper.inIfPresent(PurchaseOrder::getClinicCode, pageRequest.getClinicCodes());
        queryWrapper.inIfPresent(PurchaseOrder::getSupplierCode, pageRequest.getSupplierCodes());
        queryWrapper.inIfPresent(PurchaseOrder::getReceiveStatus, pageRequest.getReceiveStatuses());
        queryWrapper.inIfPresent(PurchaseOrder::getPaymentStatus, pageRequest.getPaymentStatuses());
        queryWrapper.geIfPresent(PurchaseOrder::getCreateTime,
                DateTimeUtil.startTimeOfDate(pageRequest.getCreateDateMin()));
        queryWrapper.leIfPresent(PurchaseOrder::getCreateTime,
                DateTimeUtil.endTimeOfDate(pageRequest.getCreateDateMax()));

        queryWrapper.orderByDesc(PurchaseOrder::getCode);

        Page<PurchaseOrder> pageCondition = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<PurchaseOrder> pageData = this.selectPage(pageCondition, queryWrapper);
        return PageResponse.success(pageData.getRecords(),
                pageData.getTotal(),
                (int) pageCondition.getSize(),
                (int) pageCondition.getCurrent());
    }

}

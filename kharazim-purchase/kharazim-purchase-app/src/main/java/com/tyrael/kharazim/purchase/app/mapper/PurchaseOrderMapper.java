package com.tyrael.kharazim.purchase.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import com.tyrael.kharazim.mybatis.LambdaQueryWrapperX;
import com.tyrael.kharazim.purchase.app.domain.PurchaseOrder;
import com.tyrael.kharazim.purchase.app.vo.purchaseorder.PagePurchaseOrderRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import static com.tyrael.kharazim.base.util.DateTimeUtil.endTimeOfDate;
import static com.tyrael.kharazim.base.util.DateTimeUtil.startTimeOfDate;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Mapper
public interface PurchaseOrderMapper extends BasePageMapper<PurchaseOrder> {

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
        queryWrapper.geIfPresent(PurchaseOrder::getCreateTime, startTimeOfDate(pageRequest.getCreateDateMin()));
        queryWrapper.leIfPresent(PurchaseOrder::getCreateTime, endTimeOfDate(pageRequest.getCreateDateMax()));

        queryWrapper.orderByDesc(PurchaseOrder::getCode);

        return this.page(pageRequest, queryWrapper);
    }

}

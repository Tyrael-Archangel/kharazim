package com.tyrael.kharazim.application.purchase.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrderReceiveRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Mapper
public interface PurchaseOrderReceiveRecordMapper extends BaseMapper<PurchaseOrderReceiveRecord> {

    /**
     * list by purchaseOrderCode
     *
     * @param purchaseOrderCode 采购单号
     * @return PurchaseOrderReceiveRecords
     */
    default List<PurchaseOrderReceiveRecord> listByPurchaseOrderCode(String purchaseOrderCode) {
        if (StringUtils.isBlank(purchaseOrderCode)) {
            return new ArrayList<>();
        }
        return listByPurchaseOrderCodes(Collections.singletonList(purchaseOrderCode));
    }

    /**
     * list by purchaseOrderCodes
     *
     * @param purchaseOrderCodes 采购单号
     * @return PurchaseOrderReceiveRecords
     */
    default List<PurchaseOrderReceiveRecord> listByPurchaseOrderCodes(Collection<String> purchaseOrderCodes) {
        if (purchaseOrderCodes == null || purchaseOrderCodes.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<PurchaseOrderReceiveRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PurchaseOrderReceiveRecord::getPurchaseOrderCode, purchaseOrderCodes);
        return selectList(queryWrapper);
    }

}

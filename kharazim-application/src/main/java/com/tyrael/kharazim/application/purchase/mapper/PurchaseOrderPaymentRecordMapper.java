package com.tyrael.kharazim.application.purchase.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrderPaymentRecord;
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
public interface PurchaseOrderPaymentRecordMapper extends BaseMapper<PurchaseOrderPaymentRecord> {

    /**
     * list by purchaseOrderCode
     *
     * @param purchaseOrderCode 采购单号
     * @return PurchaseOrderPaymentRecords
     */
    default List<PurchaseOrderPaymentRecord> listByPurchaseOrderCode(String purchaseOrderCode) {
        if (StringUtils.isBlank(purchaseOrderCode)) {
            return new ArrayList<>();
        }
        return listByPurchaseOrderCodes(Collections.singletonList(purchaseOrderCode));
    }

    /**
     * list by purchaseOrderCodes
     *
     * @param purchaseOrderCodes 采购单号
     * @return PurchaseOrderPaymentRecords
     */
    default List<PurchaseOrderPaymentRecord> listByPurchaseOrderCodes(Collection<String> purchaseOrderCodes) {
        if (purchaseOrderCodes == null || purchaseOrderCodes.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<PurchaseOrderPaymentRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PurchaseOrderPaymentRecord::getPurchaseOrderCode, purchaseOrderCodes);
        return selectList(queryWrapper);
    }

}

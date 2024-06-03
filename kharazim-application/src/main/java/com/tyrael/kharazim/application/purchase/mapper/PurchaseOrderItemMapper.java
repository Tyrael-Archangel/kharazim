package com.tyrael.kharazim.application.purchase.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrderItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Mapper
public interface PurchaseOrderItemMapper extends BaseMapper<PurchaseOrderItem> {

    /**
     * batch insert
     *
     * @param items PurchaseOrderItems
     */
    default void batchInsert(List<PurchaseOrderItem> items) {
        Db.saveBatch(items);
    }

    /**
     * list by purchaseOrderCode
     *
     * @param purchaseOrderCode 采购单号
     * @return PurchaseOrderItems
     */
    default List<PurchaseOrderItem> listByPurchaseOrderCode(String purchaseOrderCode) {
        if (StringUtils.isBlank(purchaseOrderCode)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<PurchaseOrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchaseOrderItem::getPurchaseOrderCode, purchaseOrderCode);
        return selectList(queryWrapper);
    }

}

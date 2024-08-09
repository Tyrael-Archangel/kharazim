package com.tyrael.kharazim.application.purchase.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrderReceiveRecordItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Mapper
public interface PurchaseOrderReceiveRecordItemMapper extends BaseMapper<PurchaseOrderReceiveRecordItem> {

    /**
     * 批量插入
     *
     * @param items PurchaseOrderReceiveRecordItems
     */
    default void batchInsert(Collection<PurchaseOrderReceiveRecordItem> items) {
        Db.saveBatch(items);
    }

    /**
     * list by serialCodes
     *
     * @param serialCodes 收货记录流水号
     * @return PurchaseOrderReceiveRecordItems
     */
    default List<PurchaseOrderReceiveRecordItem> listBySerialCodes(Collection<String> serialCodes) {
        if (serialCodes == null || serialCodes.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<PurchaseOrderReceiveRecordItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PurchaseOrderReceiveRecordItem::getReceiveSerialCode, serialCodes);
        return selectList(queryWrapper);
    }

}

package com.tyrael.kharazim.application.settlement.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.tyrael.kharazim.application.settlement.domain.SettlementOrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Mapper
public interface SettlementOrderItemMapper extends BaseMapper<SettlementOrderItem> {

    /**
     * 批量保存
     *
     * @param items 结算单商品明细
     */
    default void batchInsert(List<SettlementOrderItem> items) {
        Db.saveBatch(items);
    }

    /**
     * list by settlementOrderCode
     *
     * @param settlementOrderCode 结算单编码
     * @return 结算单商品明细
     */
    default List<SettlementOrderItem> listBySettlementOrderCode(String settlementOrderCode) {
        return listBySettlementOrderCodes(Collections.singleton(settlementOrderCode));
    }

    /**
     * list by settlementOrderCodes
     *
     * @param settlementOrderCodes 结算单编码
     * @return 结算单商品明细
     */
    default List<SettlementOrderItem> listBySettlementOrderCodes(Collection<String> settlementOrderCodes) {
        if (settlementOrderCodes == null || settlementOrderCodes.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<SettlementOrderItem> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(SettlementOrderItem::getSettlementOrderCode, settlementOrderCodes);
        return selectList(queryWrapper);
    }

}

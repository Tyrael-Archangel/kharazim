package com.tyrael.kharazim.application.pharmacy.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.tyrael.kharazim.application.pharmacy.domain.InboundOrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/7/12
 */
@Mapper
public interface InboundOrderItemMapper extends BaseMapper<InboundOrderItem> {

    /**
     * 批量保存
     *
     * @param items 入库单商品明细
     */
    default void batchInsert(List<InboundOrderItem> items) {
        Db.saveBatch(items);
    }

    /**
     * list by inboundOrderCode
     *
     * @param inboundOrderCode inboundOrderCode
     * @return InboundOrderItems
     */
    default List<InboundOrderItem> listByInboundOrderCode(String inboundOrderCode) {
        LambdaQueryWrapper<InboundOrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InboundOrderItem::getInboundOrderCode, inboundOrderCode);
        return this.selectList(queryWrapper);
    }

}

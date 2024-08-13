package com.tyrael.kharazim.application.pharmacy.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.tyrael.kharazim.application.pharmacy.domain.InboundOrderItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        if (StringUtils.isBlank(inboundOrderCode)) {
            return new ArrayList<>();
        }
        return listByInboundOrderCodes(Collections.singleton(inboundOrderCode));
    }

    /**
     * list by inboundOrderCodes
     *
     * @param inboundOrderCodes inboundOrderCodes
     * @return InboundOrderItems
     */
    default List<InboundOrderItem> listByInboundOrderCodes(Collection<String> inboundOrderCodes) {
        if (inboundOrderCodes == null || inboundOrderCodes.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<InboundOrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(InboundOrderItem::getInboundOrderCode, inboundOrderCodes);
        return this.selectList(queryWrapper);
    }

}

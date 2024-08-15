package com.tyrael.kharazim.application.pharmacy.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.tyrael.kharazim.application.pharmacy.domain.OutboundOrderItem;
import com.tyrael.kharazim.common.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@Mapper
public interface OutboundOrderItemMapper extends BaseMapper<OutboundOrderItem> {

    /**
     * 批量保存
     *
     * @param items 出库单商品明细
     */
    default void batchInsert(List<OutboundOrderItem> items) {
        Db.saveBatch(items);
    }

    /**
     * list by outboundOrderCode
     *
     * @param outboundOrderCode outboundOrderCode
     * @return OutboundOrderItems
     */
    default List<OutboundOrderItem> listByOutboundOrderCode(String outboundOrderCode) {
        if (StringUtils.isBlank(outboundOrderCode)) {
            return new ArrayList<>();
        }
        return listByOutboundOrderCodes(Collections.singletonList(outboundOrderCode));
    }

    /**
     * list by outboundOrderCodes
     *
     * @param outboundOrderCodes outboundOrderCodes
     * @return OutboundOrderItems
     */
    default List<OutboundOrderItem> listByOutboundOrderCodes(Collection<String> outboundOrderCodes) {
        if (CollectionUtils.isEmpty(outboundOrderCodes)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<OutboundOrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(OutboundOrderItem::getOutboundOrderCode, outboundOrderCodes);
        return selectList(queryWrapper);
    }

}

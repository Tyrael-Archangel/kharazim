package com.tyrael.kharazim.application.pharmacy.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyrael.kharazim.application.pharmacy.domain.InboundOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/7/12
 */
@Mapper
public interface InboundOrderMapper extends BaseMapper<InboundOrder> {

    /**
     * find by code FOR UPDATE
     *
     * @param code InboundOrder.code
     * @return InboundOrder
     */
    default InboundOrder findByCodeForUpdate(String code) {
        LambdaQueryWrapper<InboundOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InboundOrder::getCode, code);
        queryWrapper.last(" FOR UPDATE");
        return selectOne(queryWrapper);
    }

}

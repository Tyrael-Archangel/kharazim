package com.tyrael.kharazim.application.purchase.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyrael.kharazim.application.purchase.domain.PurchaseOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Mapper
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {
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

}

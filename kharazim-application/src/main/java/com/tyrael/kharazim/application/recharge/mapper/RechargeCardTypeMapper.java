package com.tyrael.kharazim.application.recharge.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.recharge.domain.RechargeCardType;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/1/25
 */
@Mapper
public interface RechargeCardTypeMapper extends BaseMapper<RechargeCardType> {

    /**
     * find by code
     *
     * @param code 储值卡项编码
     * @return RechargeCardType
     */
    default RechargeCardType findByCode(String code) {
        LambdaQueryWrapper<RechargeCardType> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(RechargeCardType::getCode, code);
        return selectOne(queryWrapper);
    }

}

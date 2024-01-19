package com.tyrael.kharazim.application.customer.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.customer.domain.Family;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/1/19
 */
@Mapper
public interface FamilyMapper extends BaseMapper<Family> {

    /**
     * find by code
     *
     * @param code 家庭编码
     * @return Family
     */
    default Family findByCode(String code) {
        LambdaQueryWrapper<Family> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Family::getCode, code);
        return selectOne(queryWrapper);
    }

}

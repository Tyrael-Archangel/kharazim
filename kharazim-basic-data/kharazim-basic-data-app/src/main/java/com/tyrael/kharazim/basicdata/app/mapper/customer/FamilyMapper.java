package com.tyrael.kharazim.basicdata.app.mapper.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.app.domain.customer.Family;
import com.tyrael.kharazim.basicdata.app.dto.customer.family.PageFamilyRequest;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import com.tyrael.kharazim.mybatis.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/19
 */
@Mapper
public interface FamilyMapper extends BasePageMapper<Family> {

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


    /**
     * list by codes
     *
     * @param familyCodes 家庭编码
     * @return Families
     */
    default List<Family> listByCodes(Collection<String> familyCodes) {
        if (CollectionUtils.isEmpty(familyCodes)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Family> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Family::getCode, familyCodes);
        return selectList(queryWrapper);
    }

    /**
     * page family
     *
     * @param pageRequest {@link PageFamilyRequest}
     * @return Page of Families
     */
    default PageResponse<Family> page(PageFamilyRequest pageRequest) {
        LambdaQueryWrapperX<Family> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfHasText(Family::getCode, pageRequest.getFamilyCode())
                .eqIfHasText(Family::getName, pageRequest.getFamilyName());

        return page(pageRequest, queryWrapper);
    }
}

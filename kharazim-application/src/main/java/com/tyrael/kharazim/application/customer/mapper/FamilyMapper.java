package com.tyrael.kharazim.application.customer.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.customer.domain.Family;
import com.tyrael.kharazim.application.customer.vo.family.PageFamilyRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
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

        Page<Family> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<Family> familyPage = selectPage(page, queryWrapper);
        return PageResponse.success(familyPage.getRecords(),
                familyPage.getTotal(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }
}

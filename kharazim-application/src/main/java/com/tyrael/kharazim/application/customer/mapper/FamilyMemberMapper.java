package com.tyrael.kharazim.application.customer.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.customer.domain.FamilyMember;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/19
 */
@Mapper
public interface FamilyMemberMapper extends BaseMapper<FamilyMember> {

    /**
     * list by familyCode
     *
     * @param familyCode familyCode
     * @return familyMembers
     */
    default List<FamilyMember> listByFamilyCode(String familyCode) {
        LambdaQueryWrapper<FamilyMember> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(FamilyMember::getFamilyCode, familyCode);
        return selectList(queryWrapper);
    }

    /**
     * list by familyCodes
     *
     * @param familyCodes familyCodes
     * @return familyMembers
     */
    default List<FamilyMember> listByFamilyCodes(Collection<String> familyCodes) {
        if (CollectionUtils.isEmpty(familyCodes)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<FamilyMember> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(FamilyMember::getFamilyCode, familyCodes);
        return selectList(queryWrapper);
    }

}

package com.tyrael.kharazim.application.customer.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
     * find by customerCode
     *
     * @param familyCode   家庭编码
     * @param customerCode 会员编码
     * @return FamilyMember
     */
    default FamilyMember findByCustomerCode(String familyCode, String customerCode) {
        LambdaQueryWrapper<FamilyMember> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(FamilyMember::getFamilyCode, familyCode)
                .eq(FamilyMember::getCustomerCode, customerCode);
        return selectOne(queryWrapper);
    }

    /**
     * list by customerCode
     *
     * @param customerCode 会员编码
     * @return FamilyMember
     */
    default List<FamilyMember> listByCustomerCode(String customerCode) {
        LambdaQueryWrapper<FamilyMember> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(FamilyMember::getCustomerCode, customerCode);
        return selectList(queryWrapper);
    }

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

    /**
     * 会员离开指定家庭
     *
     * @param familyCode   familyCode
     * @param customerCode 会员编码
     */
    default void leave(String familyCode, String customerCode) {
        LambdaUpdateWrapper<FamilyMember> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(FamilyMember::getFamilyCode, familyCode)
                .eq(FamilyMember::getCustomerCode, customerCode)
                .set(FamilyMember::getDeleted, true)
                .set(FamilyMember::getDeletedTimestamp, System.currentTimeMillis());
        this.update(null, updateWrapper);
    }

}

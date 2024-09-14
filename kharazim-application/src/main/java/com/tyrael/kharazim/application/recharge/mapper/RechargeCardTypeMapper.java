package com.tyrael.kharazim.application.recharge.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.base.BasePageMapper;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.recharge.domain.RechargeCardType;
import com.tyrael.kharazim.application.recharge.vo.ListRechargeCardTypeRequest;
import com.tyrael.kharazim.application.recharge.vo.PageRechargeCardTypeRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.util.CollectionUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/1/25
 */
@Mapper
public interface RechargeCardTypeMapper extends BasePageMapper<RechargeCardType> {

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

    /**
     * page
     *
     * @param pageRequest {@link PageRechargeCardTypeRequest}
     * @return {@link PageResponse}<{@link RechargeCardType}>
     */
    default PageResponse<RechargeCardType> page(PageRechargeCardTypeRequest pageRequest) {
        LambdaQueryWrapperX<RechargeCardType> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.likeIfPresent(RechargeCardType::getName, pageRequest.getName())
                .eqIfPresent(RechargeCardType::getCanCreateNewCard, pageRequest.getCanCreateNewCard());
        queryWrapper.orderByAsc(RechargeCardType::getId);

        return page(pageRequest, queryWrapper);
    }

    /**
     * list all
     *
     * @return RechargeCardTypes
     */
    default List<RechargeCardType> listAll() {
        return selectList(new LambdaQueryWrapper<>());
    }

    /**
     * list
     *
     * @param listRequest {@link ListRechargeCardTypeRequest}
     * @return RechargeCardTypes
     */
    default List<RechargeCardType> list(ListRechargeCardTypeRequest listRequest) {
        LambdaQueryWrapperX<RechargeCardType> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.likeIfPresent(RechargeCardType::getName, listRequest.getName())
                .eqIfPresent(RechargeCardType::getCanCreateNewCard, listRequest.getCanCreateNewCard());
        queryWrapper.orderByAsc(RechargeCardType::getId);
        return selectList(queryWrapper);
    }

    /**
     * list by codes
     *
     * @param codes 储值卡项编码
     * @return RechargeCardTypes
     */
    default List<RechargeCardType> listByCodes(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<RechargeCardType> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(RechargeCardType::getCode, codes);
        return selectList(queryWrapper);
    }

    /**
     * map by codes
     *
     * @param codes codes
     * @return Map<code, RechargeCardType>
     */
    default Map<String, RechargeCardType> mapByCodes(Collection<String> codes) {
        List<RechargeCardType> rechargeCardTypes = listByCodes(codes);
        return rechargeCardTypes.stream()
                .collect(Collectors.toMap(RechargeCardType::getCode, e -> e));
    }
}

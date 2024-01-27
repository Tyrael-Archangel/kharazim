package com.tyrael.kharazim.application.recharge.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.recharge.domain.RechargeCardType;
import com.tyrael.kharazim.application.recharge.vo.PageRechargeCardTypeRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
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

        Page<RechargeCardType> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<RechargeCardType> cardTypePage = selectPage(page, queryWrapper);
        return PageResponse.success(cardTypePage.getRecords(),
                cardTypePage.getTotal(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }

}

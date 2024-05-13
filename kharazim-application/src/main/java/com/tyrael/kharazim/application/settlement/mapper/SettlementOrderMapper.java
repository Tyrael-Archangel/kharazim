package com.tyrael.kharazim.application.settlement.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.settlement.domain.SettlementOrder;
import com.tyrael.kharazim.application.settlement.enums.SettlementOrderStatus;
import com.tyrael.kharazim.application.settlement.vo.PageSettlementOrderRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Mapper
public interface SettlementOrderMapper extends BaseMapper<SettlementOrder> {

    /**
     * find by code
     *
     * @param code 结算单编码
     * @return 结算单
     */
    default SettlementOrder findByCode(String code) {
        LambdaQueryWrapper<SettlementOrder> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SettlementOrder::getCode, code);
        return selectOne(queryWrapper);
    }

    /**
     * 结算单分页
     *
     * @param pageRequest {@link PageSettlementOrderRequest}
     * @return 结算单分页数据
     */
    default PageResponse<SettlementOrder> page(PageSettlementOrderRequest pageRequest) {
        LambdaQueryWrapperX<SettlementOrder> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfHasText(SettlementOrder::getCode, pageRequest.getSettlementOrderCode());
        queryWrapper.eqIfHasText(SettlementOrder::getCustomerCode, pageRequest.getCustomerCode());
        queryWrapper.inIfPresent(SettlementOrder::getClinicCode, pageRequest.getClinicCodes());
        queryWrapper.eqIfPresent(SettlementOrder::getStatus, pageRequest.getStatus());
        queryWrapper.orderByDesc(SettlementOrder::getCode);

        Page<SettlementOrder> pageCondition = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<SettlementOrder> pageData = selectPage(pageCondition, queryWrapper);
        return PageResponse.success(pageData.getRecords(),
                pageData.getTotal(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }

    /**
     * 保存已结算
     *
     * @param settlementOrder 结算单
     * @return success
     */
    default boolean saveSettlement(SettlementOrder settlementOrder) {
        LambdaUpdateWrapper<SettlementOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SettlementOrder::getId, settlementOrder.getId())
                .eq(SettlementOrder::getStatus, SettlementOrderStatus.UNPAID);
        updateWrapper.set(SettlementOrder::getStatus, settlementOrder.getStatus())
                .set(SettlementOrder::getSettlementTime, settlementOrder.getSettlementTime())
                .set(SettlementOrder::getUpdaterCode, settlementOrder.getUpdaterCode())
                .set(SettlementOrder::getUpdater, settlementOrder.getUpdater())
                .set(SettlementOrder::getUpdateTime, settlementOrder.getUpdateTime());
        return this.update(null, updateWrapper) == 1;
    }

}

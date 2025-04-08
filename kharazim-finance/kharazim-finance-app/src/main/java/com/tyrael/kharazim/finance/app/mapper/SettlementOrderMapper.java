package com.tyrael.kharazim.finance.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.finance.app.domain.SettlementOrder;
import com.tyrael.kharazim.finance.app.enums.SettlementOrderStatus;
import com.tyrael.kharazim.finance.app.vo.settlement.PageSettlementOrderRequest;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import com.tyrael.kharazim.mybatis.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Mapper
public interface SettlementOrderMapper extends BasePageMapper<SettlementOrder> {

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
        queryWrapper.eqIfHasText(SettlementOrder::getSourcePrescriptionCode, pageRequest.getSourcePrescriptionCode());
        queryWrapper.eqIfHasText(SettlementOrder::getCustomerCode, pageRequest.getCustomerCode());
        queryWrapper.inIfPresent(SettlementOrder::getClinicCode, pageRequest.getClinicCodes());
        queryWrapper.eqIfPresent(SettlementOrder::getStatus, pageRequest.getStatus());
        queryWrapper.orderByDesc(SettlementOrder::getCode);

        return page(pageRequest, queryWrapper);
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

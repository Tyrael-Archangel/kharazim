package com.tyrael.kharazim.application.recharge.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.recharge.domain.CustomerRechargeCard;
import com.tyrael.kharazim.application.recharge.enums.CustomerRechargeCardStatus;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeCardPageRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/2/1
 */
@Mapper
public interface CustomerRechargeCardMapper extends BaseMapper<CustomerRechargeCard> {

    /**
     * find by code
     *
     * @param code 储值单编码
     * @return {@link CustomerRechargeCard}
     */
    default CustomerRechargeCard findByCode(String code) {
        LambdaQueryWrapper<CustomerRechargeCard> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CustomerRechargeCard::getCode, code);
        return selectOne(queryWrapper);
    }

    /**
     * page
     *
     * @param pageRequest CustomerRechargeCardPageRequest
     * @return pageResponse
     */
    default PageResponse<CustomerRechargeCard> page(CustomerRechargeCardPageRequest pageRequest) {
        LambdaQueryWrapperX<CustomerRechargeCard> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfHasText(CustomerRechargeCard::getCode, pageRequest.getCode())
                .eqIfHasText(CustomerRechargeCard::getCustomerCode, pageRequest.getCustomerCode())
                .eqIfHasText(CustomerRechargeCard::getTraderUserCode, pageRequest.getTraderUserCode())
                .geIfPresent(CustomerRechargeCard::getRechargeDate, pageRequest.getStartDate())
                .leIfPresent(CustomerRechargeCard::getRechargeDate, pageRequest.getEndDate());

        Page<CustomerRechargeCard> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<CustomerRechargeCard> pageResponse = selectPage(page, queryWrapper);
        return PageResponse.success(pageResponse.getRecords(),
                pageResponse.getTotal(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }

    /**
     * 查询会员有效的储值单
     *
     * @param customerCode 会员编码
     * @return 会员有效的储值单
     */
    default List<CustomerRechargeCard> listEffectiveCards(String customerCode) {
        // 先在内存中过滤，如果后期数据量大，则在DB中过滤
        List<CustomerRechargeCard> customerRechargeCards = this.listByCustomerCode(customerCode);
        return customerRechargeCards.stream()
                .filter(CustomerRechargeCard::effective)
                .collect(Collectors.toList());
    }

    /**
     * 根据会员编码查询
     *
     * @param customerCode 会员编码
     * @return CustomerRechargeCards
     */
    default List<CustomerRechargeCard> listByCustomerCode(String customerCode) {
        LambdaQueryWrapper<CustomerRechargeCard> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CustomerRechargeCard::getCustomerCode, customerCode);
        return selectList(queryWrapper);
    }

    /**
     * 标记为已收款
     *
     * @param customerRechargeCard entity
     * @return updatedRows
     */
    default int markPaid(CustomerRechargeCard customerRechargeCard) {
        LambdaUpdateWrapper<CustomerRechargeCard> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(CustomerRechargeCard::getId, customerRechargeCard.getId())
                .eq(CustomerRechargeCard::getStatus, CustomerRechargeCardStatus.UNPAID);

        updateWrapper.set(CustomerRechargeCard::getStatus, customerRechargeCard.getStatus())
                .set(CustomerRechargeCard::getUpdaterCode, customerRechargeCard.getUpdaterCode())
                .set(CustomerRechargeCard::getUpdater, customerRechargeCard.getUpdater())
                .set(CustomerRechargeCard::getUpdateTime, customerRechargeCard.getUpdateTime());
        return this.update(null, updateWrapper);
    }

    /**
     * 保存退卡
     *
     * @param rechargeCard entity
     * @return updatedRows
     */
    default int chargeback(CustomerRechargeCard rechargeCard) {
        LambdaUpdateWrapper<CustomerRechargeCard> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(CustomerRechargeCard::getId, rechargeCard.getId())
                .eq(CustomerRechargeCard::getStatus, CustomerRechargeCardStatus.PAID);

        updateWrapper.set(CustomerRechargeCard::getStatus, rechargeCard.getStatus())
                .set(CustomerRechargeCard::getChargebackAmount, rechargeCard.getChargebackAmount())
                .set(CustomerRechargeCard::getChargebackUserCode, rechargeCard.getChargebackUserCode())
                .set(CustomerRechargeCard::getUpdaterCode, rechargeCard.getUpdaterCode())
                .set(CustomerRechargeCard::getUpdater, rechargeCard.getUpdater())
                .set(CustomerRechargeCard::getUpdateTime, rechargeCard.getUpdateTime());
        return this.update(null, updateWrapper);
    }

    /**
     * 标记为已退款
     *
     * @param customerRechargeCard entity
     * @return updatedRows
     */
    default int markRefunded(CustomerRechargeCard customerRechargeCard) {
        LambdaUpdateWrapper<CustomerRechargeCard> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(CustomerRechargeCard::getId, customerRechargeCard.getId())
                .eq(CustomerRechargeCard::getStatus, CustomerRechargeCardStatus.WAIT_REFUND);

        updateWrapper.set(CustomerRechargeCard::getStatus, customerRechargeCard.getStatus())
                .set(CustomerRechargeCard::getUpdaterCode, customerRechargeCard.getUpdaterCode())
                .set(CustomerRechargeCard::getUpdater, customerRechargeCard.getUpdater())
                .set(CustomerRechargeCard::getUpdateTime, customerRechargeCard.getUpdateTime());
        return this.update(null, updateWrapper);
    }

}

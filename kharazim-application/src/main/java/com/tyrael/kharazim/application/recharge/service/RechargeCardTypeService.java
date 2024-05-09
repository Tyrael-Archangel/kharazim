package com.tyrael.kharazim.application.recharge.service;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.recharge.vo.*;
import com.tyrael.kharazim.common.dto.PageResponse;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/25
 */
public interface RechargeCardTypeService {

    /**
     * 新建储值卡项
     *
     * @param addRequest {@link AddRechargeCardTypeRequest}
     * @return 储值卡项编码
     */
    String create(AddRechargeCardTypeRequest addRequest);

    /**
     * 修改储值卡项
     *
     * @param modifyRequest {@link ModifyRechargeCardTypeRequest}
     * @param currentUser   操作人
     */
    void modify(ModifyRechargeCardTypeRequest modifyRequest, AuthUser currentUser);

    /**
     * 禁止发卡
     *
     * @param code        储值卡项编码
     * @param currentUser 操作人
     */
    void disableCreateNewCard(String code, AuthUser currentUser);
    /**
     * 启用发卡
     *
     * @param code        储值卡项编码
     * @param currentUser 操作人
     */
    void enableCreateNewCard(String code, AuthUser currentUser);

    /**
     * 储值卡项分页
     *
     * @param pageRequest {@link PageRechargeCardTypeRequest}
     * @return 储值卡项分页数据
     */
    PageResponse<RechargeCardTypeVO> page(PageRechargeCardTypeRequest pageRequest);

    /**
     * list
     *
     * @return 储值卡项数据
     */
    List<RechargeCardTypeVO> list(ListRechargeCardTypeRequest listRequest);

}

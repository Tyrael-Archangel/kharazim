package com.tyrael.kharazim.application.recharge.service;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.recharge.vo.AddRechargeCardTypeRequest;
import com.tyrael.kharazim.application.recharge.vo.ModifyRechargeCardTypeRequest;
import com.tyrael.kharazim.application.recharge.vo.PageRechargeCardTypeRequest;
import com.tyrael.kharazim.application.recharge.vo.RechargeCardTypeVO;
import com.tyrael.kharazim.common.dto.PageResponse;

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
     * 储值卡项分页
     *
     * @param pageRequest {@link PageRechargeCardTypeRequest}
     * @return 储值卡项分页数据
     */
    PageResponse<RechargeCardTypeVO> page(PageRechargeCardTypeRequest pageRequest);

}

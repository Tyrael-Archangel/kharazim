package com.tyrael.kharazim.pos.app;

import com.tyrael.kharazim.pos.app.vo.CreateClinicOrderCommand;
import com.tyrael.kharazim.user.sdk.model.AuthUser;

/**
 * @author Tyrael Archangel
 * @since 2025/7/15
 */
public interface ClinicOrderServiceFacade {

    /**
     * 创建诊所订单
     *
     * @param command     {@link CreateClinicOrderCommand}
     * @param currentUser 操作人
     * @return 订单号
     */
    String createOrder(CreateClinicOrderCommand command, AuthUser currentUser);

}

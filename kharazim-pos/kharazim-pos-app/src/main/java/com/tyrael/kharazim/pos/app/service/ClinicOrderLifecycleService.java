package com.tyrael.kharazim.pos.app.service;

import com.tyrael.kharazim.pos.app.vo.CreateClinicOrderCommand;
import com.tyrael.kharazim.user.sdk.model.AuthUser;

/**
 * @author Tyrael Archangel
 * @since 2025/7/18
 */
public interface ClinicOrderLifecycleService {

    /**
     * 创建诊所订单
     *
     * @param command     {@link CreateClinicOrderCommand}
     * @param currentUser 操作人
     * @return 订单号
     */
    String create(CreateClinicOrderCommand command, AuthUser currentUser);

}

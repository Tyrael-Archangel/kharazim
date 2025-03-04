package com.tyrael.kharazim.user.app.constant;

import com.tyrael.kharazim.basicdata.sdk.model.DictConstant;
import com.tyrael.kharazim.user.app.enums.EnableStatusEnum;
import com.tyrael.kharazim.user.app.enums.UserGenderEnum;

import static com.tyrael.kharazim.basicdata.sdk.model.DictConstant.dict;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
public interface UserDictConstants {

    // @formatter:off

    DictConstant ENABLE_STATUS  = dict("enable_status",     EnableStatusEnum.class,         "启用禁用状态");
    DictConstant USER_GENDER    = dict("user_gender",       UserGenderEnum.class,           "性别");

    // @formatter:on

}

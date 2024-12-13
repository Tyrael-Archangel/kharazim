package com.tyrael.kharazim.application.system.domain;

import com.tyrael.kharazim.application.system.enums.AddressLevelEnum;
import com.tyrael.kharazim.application.system.enums.MenuTypeEnum;
import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import com.tyrael.kharazim.application.user.enums.UserCertificateTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
import com.tyrael.kharazim.common.dto.BaseHasNameEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统字典编码
 *
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@SuppressWarnings("unused")
public sealed interface DictConstants permits SealedDictConstants {

    // @formatter:off

    DictConstant ENABLE_STATUS            = dict("enable_status",            EnableStatusEnum.class,         "启用禁用状态");
    DictConstant USER_GENDER              = dict("user_gender",              UserGenderEnum.class,           "性别");
    DictConstant CERTIFICATE_TYPE         = dict("certificate_type",         UserCertificateTypeEnum.class,  "证件类型");
    DictConstant MENU_TYPE                = dict("menu_type",                MenuTypeEnum.class,             "系统菜单类型");
    DictConstant SYSTEM_ADDRESS_LEVEL     = dict("system_address_level",     AddressLevelEnum.class,         "地址行政等级");

    DictConstant CUSTOMER_SOURCE_CHANNEL  = dict("customer_source_channel", "会员来源渠道");
    DictConstant INSURANCE_COMPANY        = dict("insurance_company",       "保险公司");
    DictConstant CUSTOMER_TAG             = dict("customer_tag",            "会员标签");
    DictConstant COMMUNICATION_TYPE       = dict("communication_type",      "会员沟通记录-类型");
    DictConstant COMMUNICATION_EVALUATE   = dict("communication_evaluate",  "会员沟通记录-评价");

    // @formatter:on

    private static <T extends Enum<T> & BaseHasNameEnum<T>> DictConstant.EnumDictConstant<T> dict(
            String code, Class<T> relatedEnum, String desc) {
        return new DictConstant.EnumDictConstant<>(code, relatedEnum, desc);
    }

    private static DictConstant dict(String code, String desc) {
        return new DictConstant(code, desc);
    }

    static List<DictConstant> allDictConstants() {
        return List.copyOf(SealedDictConstants.dictConstantsCache.values());
    }

    static DictConstant getDictConstant(String code) {
        return SealedDictConstants.dictConstantsCache.get(code);
    }

}

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class SealedDictConstants implements DictConstants {

    static final Map<String, DictConstant> dictConstantsCache = Collections.unmodifiableMap(new LinkedHashMap<>() {
        {
            for (Field field : DictConstants.class.getDeclaredFields()) {
                if (DictConstant.class.isAssignableFrom(field.getType())) {
                    DictConstant dictConstant;
                    try {
                        dictConstant = (DictConstant) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    if (containsKey(dictConstant.getCode())) {
                        throw new IllegalArgumentException("Duplicate dict code '" + dictConstant.getCode() + "'");
                    }
                    put(dictConstant.getCode(), dictConstant);
                }
            }
        }
    });

}



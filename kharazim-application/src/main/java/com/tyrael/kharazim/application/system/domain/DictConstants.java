package com.tyrael.kharazim.application.system.domain;

import com.tyrael.kharazim.application.system.enums.AddressLevelEnum;
import com.tyrael.kharazim.application.system.enums.MenuTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserCertificateTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
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

    DictConstant SYSTEM_ADDRESS_LEVEL = new DictConstant.EnumDictConstant<>("system_address_level", "地址行政等级", AddressLevelEnum.class);
    DictConstant USER_GENDER = new DictConstant.EnumDictConstant<>("user_gender", "性别", UserGenderEnum.class);
    DictConstant CERTIFICATE_TYPE = new DictConstant.EnumDictConstant<>("certificate_code", "证件类型", UserCertificateTypeEnum.class);
    DictConstant MENU_TYPE = new DictConstant.EnumDictConstant<>("menu_type", "系统菜单类型", MenuTypeEnum.class);
    DictConstant CUSTOMER_SOURCE_CHANNEL = new DictConstant("customer_source_channel", "会员来源渠道");
    DictConstant INSURANCE_COMPANY = new DictConstant("insurance_company", "保险公司");
    DictConstant CUSTOMER_TAG = new DictConstant("customer_tag", "会员标签");
    DictConstant CUSTOMER_COMMUNICATION_TYPE = new DictConstant("communication_type", "会员沟通记录-类型");
    DictConstant CUSTOMER_COMMUNICATION_EVALUATE = new DictConstant("communication_evaluate", "会员沟通记录-评价");

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



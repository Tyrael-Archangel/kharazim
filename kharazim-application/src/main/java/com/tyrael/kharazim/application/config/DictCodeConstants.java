package com.tyrael.kharazim.application.config;

import com.tyrael.kharazim.application.user.enums.UserCertificateTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
import lombok.Getter;

/**
 * 系统字典编码
 *
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Getter
public enum DictCodeConstants {

    USER_GENDER("user_gender", UserGenderEnum.class),
    CERTIFICATE_TYPE("certificate_code", UserCertificateTypeEnum.class),
    CUSTOMER_SOURCE_CHANNEL("customer_source_channel", "会员来源渠道"),
    INSURANCE_COMPANY("insurance_company", "保险公司"),
    CUSTOMER_TAG("customer_tag", "会员标签"),
    CUSTOMER_COMMUNICATION_TYPE("communication_type", "会员沟通记录-类型"),
    CUSTOMER_COMMUNICATION_EVALUATE("communication_evaluate", "会员沟通记录-评价"),
    PRODUCT_BRAND("product_brand", "商品品牌"),
    MEDICATION_FREQUENCY("medication_frequency", "用药频率，例如每日三次、每周两次"),
    MEDICATION_USAGE("medication_usage", "药品用法，例如口服、水煎服"),
    ;

    /**
     * 字典编码
     */
    private final String dictCode;
    /**
     * 描述
     */
    private final String desc;
    /**
     * 相关的枚举类
     */
    private final Class<?> relatedEnum;

    DictCodeConstants(String dictCode, String desc) {
        this(dictCode, desc, null);
    }

    DictCodeConstants(String dictCode, Class<?> relatedEnum) {
        this(dictCode, null, relatedEnum);
    }

    DictCodeConstants(String dictCode, String desc, Class<?> relatedEnum) {
        this.dictCode = dictCode;
        this.desc = desc;
        this.relatedEnum = relatedEnum;
    }

}

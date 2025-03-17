package com.tyrael.kharazim.basicdata.app.constant;

import com.tyrael.kharazim.basicdata.app.enums.*;
import com.tyrael.kharazim.basicdata.sdk.model.DictConstant;

import static com.tyrael.kharazim.basicdata.sdk.model.DictConstant.dict;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
public interface BasicDataDictConstants {

    // @formatter:off

    DictConstant CUSTOMER_GENDER            = dict("customer_gender",            CustomerGenderEnum.class,           "会员性别");
    DictConstant CUSTOMER_CERTIFICATE_TYPE  = dict("customer_certificate_type",  CustomerCertificateTypeEnum.class,  "会员证件类型");
    DictConstant SYSTEM_ADDRESS_LEVEL       = dict("system_address_level",       AddressLevelEnum.class,             "地址行政等级");
    DictConstant CLINIC_STATUS              = dict("clinic_status",              ClinicStatus.class,                 "诊所状态");
    DictConstant CUSTOMER_RECHARGE_STATUS   = dict("customer_recharge_status",   CustomerRechargeCardStatus.class,   "会员储值单状态");

    DictConstant CUSTOMER_SOURCE_CHANNEL    = dict("customer_source_channel",   "会员来源渠道");
    DictConstant INSURANCE_COMPANY          = dict("insurance_company",         "保险公司");
    DictConstant CUSTOMER_TAG               = dict("customer_tag",              "会员标签");
    DictConstant COMMUNICATION_TYPE         = dict("communication_type",        "会员沟通记录-类型");
    DictConstant COMMUNICATION_EVALUATE     = dict("communication_evaluate",    "会员沟通记录-评价");

    // @formatter:on

}

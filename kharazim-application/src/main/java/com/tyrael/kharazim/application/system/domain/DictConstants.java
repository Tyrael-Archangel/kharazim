package com.tyrael.kharazim.application.system.domain;

import com.tyrael.kharazim.application.clinic.enums.ClinicStatus;
import com.tyrael.kharazim.application.pharmacy.enums.InboundOrderStatus;
import com.tyrael.kharazim.application.pharmacy.enums.InventoryChangeTypeEnum;
import com.tyrael.kharazim.application.pharmacy.enums.OutboundOrderStatus;
import com.tyrael.kharazim.application.purchase.enums.PurchasePaymentStatus;
import com.tyrael.kharazim.application.purchase.enums.PurchaseReceiveStatus;
import com.tyrael.kharazim.application.recharge.enums.CustomerRechargeCardStatus;
import com.tyrael.kharazim.application.settlement.enums.SettlementOrderStatus;
import com.tyrael.kharazim.application.skupublish.enums.SkuPublishStatus;
import com.tyrael.kharazim.application.system.enums.AddressLevelEnum;
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

    Dict ENABLE_STATUS             = dict("enable_status",             EnableStatusEnum.class,            "启用禁用状态");
    Dict USER_GENDER               = dict("user_gender",               UserGenderEnum.class,              "性别");
    Dict CERTIFICATE_TYPE          = dict("certificate_type",          UserCertificateTypeEnum.class,     "证件类型");
    Dict SYSTEM_ADDRESS_LEVEL      = dict("system_address_level",      AddressLevelEnum.class,            "地址行政等级");
    Dict CLINIC_STATUS             = dict("clinic_status",             ClinicStatus.class,                "诊所状态");
    Dict PURCHASE_RECEIVE_STATUS   = dict("purchase_receive_status",   PurchaseReceiveStatus.class,       "采购收货状态");
    Dict PURCHASE_PAYMENT_STATUS   = dict("purchase_payment_status",   PurchasePaymentStatus.class,       "采购结算状态");
    Dict SETTLEMENT_ORDER_STATUS   = dict("settlement_order_status",   SettlementOrderStatus.class,       "结算单状态");
    Dict CUSTOMER_RECHARGE_STATUS  = dict("customer_recharge_status",  CustomerRechargeCardStatus.class,  "会员储值单状态");
    Dict SKU_PUBLISH_STATUS        = dict("sku_publish_status",        SkuPublishStatus.class,            "商品发布状态");
    Dict INVENTORY_CHANGE_TYPE     = dict("inventory_change_type",     InventoryChangeTypeEnum.class,     "库存变化类型");
    Dict INBOUND_ORDER_STATUS      = dict("inbound_order_status",      InboundOrderStatus.class,          "入库单状态");
    Dict OUTBOUND_ORDER_STATUS     = dict("outbound_order_status",     OutboundOrderStatus.class,         "出库单状态");

    Dict CUSTOMER_SOURCE_CHANNEL   = dict("customer_source_channel",   "会员来源渠道");
    Dict INSURANCE_COMPANY         = dict("insurance_company",         "保险公司");
    Dict CUSTOMER_TAG              = dict("customer_tag",              "会员标签");
    Dict COMMUNICATION_TYPE        = dict("communication_type",        "会员沟通记录-类型");
    Dict COMMUNICATION_EVALUATE    = dict("communication_evaluate",    "会员沟通记录-评价");

    // @formatter:on

    private static <T extends Enum<T> & BaseHasNameEnum<T>> Dict.EnumDict<T> dict(
            String code, Class<T> relatedEnum, String desc) {
        return new Dict.EnumDict<>(code, relatedEnum, desc);
    }

    private static Dict dict(String code, String desc) {
        return new Dict(code, desc);
    }

    static List<Dict> allDictConstants() {
        return List.copyOf(SealedDictConstants.dictConstantsCache.values());
    }

    static Dict getDictConstant(String code) {
        return SealedDictConstants.dictConstantsCache.get(code);
    }

}

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class SealedDictConstants implements DictConstants {

    static final Map<String, Dict> dictConstantsCache = Collections.unmodifiableMap(new LinkedHashMap<>() {
        {
            for (Field field : DictConstants.class.getDeclaredFields()) {
                if (Dict.class.isAssignableFrom(field.getType())) {
                    Dict dict;
                    try {
                        dict = (Dict) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    if (containsKey(dict.getCode())) {
                        throw new IllegalArgumentException("Duplicate dict code '" + dict.getCode() + "'");
                    }
                    put(dict.getCode(), dict);
                }
            }
        }
    });

}



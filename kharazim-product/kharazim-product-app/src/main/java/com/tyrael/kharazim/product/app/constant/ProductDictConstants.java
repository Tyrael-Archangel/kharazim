package com.tyrael.kharazim.product.app.constant;

import com.tyrael.kharazim.basicdata.sdk.model.DictConstant;
import com.tyrael.kharazim.product.app.enums.SkuPublishStatus;

import static com.tyrael.kharazim.basicdata.sdk.model.DictConstant.dict;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
public interface ProductDictConstants {

    // @formatter:off

    DictConstant SKU_PUBLISH_STATUS  = dict("sku_publish_status",  SkuPublishStatus.class,  "商品发布状态");

    // @formatter:on

}

package com.tyrael.kharazim.diagnosistreatment;

import com.tyrael.kharazim.basicdata.sdk.service.ClinicServiceApi;
import com.tyrael.kharazim.basicdata.sdk.service.CustomerServiceApi;
import com.tyrael.kharazim.basicdata.sdk.service.DictServiceApi;
import com.tyrael.kharazim.pharmacy.sdk.service.InventoryQueryServiceApi;
import com.tyrael.kharazim.product.sdk.service.ProductServiceApi;
import com.tyrael.kharazim.product.sdk.service.SkuPublishServiceApi;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import com.tyrael.kharazim.user.sdk.service.UserServiceApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2025/3/5
 */
@Component
public class DubboReferenceHolder {

    @DubboReference
    public UserServiceApi userServiceApi;

    @DubboReference
    public ClinicServiceApi clinicServiceApi;

    @DubboReference
    public ProductServiceApi productServiceApi;

    @DubboReference
    public DictServiceApi dictServiceApi;

    @DubboReference
    public CustomerServiceApi customerServiceApi;

    @DubboReference
    public SkuPublishServiceApi skuPublishServiceApi;

    @DubboReference
    public InventoryQueryServiceApi inventoryQueryServiceApi;

    public AuthUser mockUser() {
        return userServiceApi.mock();
    }

}

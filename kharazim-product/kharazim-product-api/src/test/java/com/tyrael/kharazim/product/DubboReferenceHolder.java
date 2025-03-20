package com.tyrael.kharazim.product;

import com.tyrael.kharazim.basicdata.sdk.service.ClinicServiceApi;
import com.tyrael.kharazim.basicdata.sdk.service.DictServiceApi;
import com.tyrael.kharazim.basicdata.sdk.service.FileServiceApi;
import com.tyrael.kharazim.basicdata.sdk.service.SupplierServiceApi;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import com.tyrael.kharazim.user.sdk.service.UserServiceApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2025/3/19
 */
@Component
public class DubboReferenceHolder {

    @DubboReference
    public UserServiceApi userServiceApi;

    @DubboReference
    public DictServiceApi dictServiceApi;

    @DubboReference
    public FileServiceApi fileServiceApi;

    @DubboReference
    public SupplierServiceApi supplierServiceApi;

    @DubboReference
    public ClinicServiceApi clinicServiceApi;

    public AuthUser mockUser() {
        return userServiceApi.mock();
    }

}

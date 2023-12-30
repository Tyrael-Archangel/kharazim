package com.tyrael.kharazim.application.clinic.service;

import com.tyrael.kharazim.application.clinic.vo.AddClinicRequest;

/**
 * @author Tyrael Archangel
 * @since 2023/12/30
 */
public interface ClinicService {

    /**
     * 新增诊所（机构）
     *
     * @param addClinicRequest {@link AddClinicRequest}
     * @return 诊所（机构）编码
     */
    String add(AddClinicRequest addClinicRequest);

}

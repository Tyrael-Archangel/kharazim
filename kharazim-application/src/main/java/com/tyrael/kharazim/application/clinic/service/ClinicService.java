package com.tyrael.kharazim.application.clinic.service;

import com.tyrael.kharazim.application.clinic.vo.AddClinicRequest;
import com.tyrael.kharazim.application.clinic.vo.ClinicVO;
import com.tyrael.kharazim.application.clinic.vo.ListClinicRequest;
import com.tyrael.kharazim.application.clinic.vo.PageClinicRequest;
import com.tyrael.kharazim.common.dto.PageResponse;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/30
 */
public interface ClinicService {

    /**
     * page
     *
     * @param pageRequest ClinicPageRequest
     * @return Clinics
     */
    PageResponse<ClinicVO> page(PageClinicRequest pageRequest);

    /**
     * list
     *
     * @param request {@link ListClinicRequest}
     * @return Clinics
     */
    List<ClinicVO> list(ListClinicRequest request);

    /**
     * 新增诊所（机构）
     *
     * @param addClinicRequest {@link AddClinicRequest}
     * @return 诊所（机构）编码
     */
    String add(AddClinicRequest addClinicRequest);

}

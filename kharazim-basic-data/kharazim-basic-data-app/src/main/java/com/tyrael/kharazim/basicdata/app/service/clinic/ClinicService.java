package com.tyrael.kharazim.basicdata.app.service.clinic;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.basicdata.app.dto.clinic.*;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/30
 */
public interface ClinicService {

    /**
     * list all
     *
     * @return clinics
     */
    List<ClinicDTO> listAll();

    /**
     * list by codes
     *
     * @param codes codes
     * @return clinics
     */
    List<ClinicDTO> listByCodes(Collection<String> codes);

    /**
     * find by code
     *
     * @param code code
     * @return clinic
     */
    ClinicDTO findByCode(String code);

    /**
     * page
     *
     * @param pageRequest ClinicPageRequest
     * @return Clinics
     */
    PageResponse<ClinicDTO> page(PageClinicRequest pageRequest);

    /**
     * export
     *
     * @param request             {@link PageClinicRequest}
     * @param httpServletResponse HttpServletResponse
     * @throws IOException IOException
     */
    void export(PageClinicRequest request, HttpServletResponse httpServletResponse) throws IOException;

    /**
     * list
     *
     * @param request {@link ListClinicRequest}
     * @return Clinics
     */
    List<ClinicDTO> list(ListClinicRequest request);

    /**
     * 新增诊所（机构）
     *
     * @param addClinicRequest {@link AddClinicRequest}
     * @return 诊所（机构）编码
     */
    String add(AddClinicRequest addClinicRequest);

    /**
     * 修改诊所（机构）
     *
     * @param modifyClinicRequest {@link ModifyClinicRequest}
     * @param currentUser         操作人
     */
    void modify(ModifyClinicRequest modifyClinicRequest, AuthUser currentUser);

}

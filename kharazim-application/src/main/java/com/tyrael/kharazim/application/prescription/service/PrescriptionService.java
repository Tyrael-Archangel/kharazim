package com.tyrael.kharazim.application.prescription.service;

import com.tyrael.kharazim.application.prescription.vo.CreatePrescriptionRequest;
import com.tyrael.kharazim.application.prescription.vo.PagePrescriptionRequest;
import com.tyrael.kharazim.application.prescription.vo.PrescriptionVO;
import com.tyrael.kharazim.common.dto.PageResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
public interface PrescriptionService {

    /**
     * 创建处方
     *
     * @param request {@link CreatePrescriptionRequest}
     * @return 处方编码
     */
    String create(CreatePrescriptionRequest request);

    /**
     * 处方详情
     *
     * @param code 处方编码
     * @return 处方详情
     */
    PrescriptionVO detail(String code);

    /**
     * 分页查询
     *
     * @param pageRequest {@link PagePrescriptionRequest}
     * @return 处方分页数据
     */
    PageResponse<PrescriptionVO> page(PagePrescriptionRequest pageRequest);

    /**
     * 处方导出
     *
     * @param pageRequest         {@link PagePrescriptionRequest}
     * @param httpServletResponse HttpServletResponse
     * @throws IOException IOException
     */
    void export(PagePrescriptionRequest pageRequest, HttpServletResponse httpServletResponse) throws IOException;

}

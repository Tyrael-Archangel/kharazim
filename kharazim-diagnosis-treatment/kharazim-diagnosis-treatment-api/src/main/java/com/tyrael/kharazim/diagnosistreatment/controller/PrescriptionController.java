package com.tyrael.kharazim.diagnosistreatment.controller;

import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.diagnosistreatment.app.service.prescription.PrescriptionLifecycleService;
import com.tyrael.kharazim.diagnosistreatment.app.service.prescription.PrescriptionQueryService;
import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.CreatePrescriptionRequest;
import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.PagePrescriptionRequest;
import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.PrescriptionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author Tyrael Archangel
 * @since 2024/3/27
 */
@RestController
@RequestMapping("/prescription")
@RequiredArgsConstructor
@Tag(name = "处方")
public class PrescriptionController {

    private final PrescriptionQueryService prescriptionQueryService;
    private final PrescriptionLifecycleService prescriptionLifecycleService;

    @GetMapping("/page")
    @Operation(summary = "处方分页")
    public PageResponse<PrescriptionVO> page(@ParameterObject PagePrescriptionRequest pageRequest) {
        return prescriptionQueryService.page(pageRequest);
    }

    @GetMapping("/export")
    @Operation(summary = "处方导出")
    public void export(@ParameterObject PagePrescriptionRequest pageRequest,
                       HttpServletResponse httpServletResponse) throws IOException {
        prescriptionQueryService.export(pageRequest, httpServletResponse);
    }

    @GetMapping("/detail/{code}")
    @Operation(summary = "处方详情")
    public DataResponse<PrescriptionVO> detail(@Schema(description = "处方编码") @PathVariable String code) {
        return DataResponse.success(prescriptionQueryService.detail(code));
    }

    @PostMapping("/create")
    @Operation(summary = "创建处方", description = "创建处方，返回处方编码")
    public DataResponse<String> create(@RequestBody @Valid CreatePrescriptionRequest request) {
        return DataResponse.success(prescriptionLifecycleService.create(request));
    }

}

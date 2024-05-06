package com.tyrael.kharazim.web.controller.prescription;

import com.tyrael.kharazim.application.prescription.service.PrescriptionService;
import com.tyrael.kharazim.application.prescription.vo.CreatePrescriptionRequest;
import com.tyrael.kharazim.application.prescription.vo.PagePrescriptionRequest;
import com.tyrael.kharazim.application.prescription.vo.PrescriptionVO;
import com.tyrael.kharazim.common.dto.DataResponse;
import com.tyrael.kharazim.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2024/3/27
 */
@RestController
@RequestMapping("/prescription")
@RequiredArgsConstructor
@Tag(name = "处方")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @GetMapping("/page")
    @Operation(summary = "处方分页")
    public PageResponse<PrescriptionVO> page(@ParameterObject PagePrescriptionRequest pageRequest) {
        return prescriptionService.page(pageRequest);
    }

    @GetMapping("/detail/{code}")
    @Operation(summary = "处方详情")
    public DataResponse<PrescriptionVO> detail(@Schema(description = "处方编码") @PathVariable String code) {
        return DataResponse.ok(prescriptionService.detail(code));
    }

    @PostMapping("/create")
    @Operation(summary = "创建处方", description = "创建处方，返回处方编码")
    public DataResponse<String> create(@RequestBody @Valid CreatePrescriptionRequest request) {
        return DataResponse.ok(prescriptionService.create(request));
    }

}

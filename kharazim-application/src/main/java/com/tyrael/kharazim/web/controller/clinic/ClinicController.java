package com.tyrael.kharazim.web.controller.clinic;

import com.tyrael.kharazim.application.clinic.service.ClinicService;
import com.tyrael.kharazim.application.clinic.vo.AddClinicRequest;
import com.tyrael.kharazim.application.clinic.vo.ClinicVO;
import com.tyrael.kharazim.application.clinic.vo.ListClinicRequest;
import com.tyrael.kharazim.application.clinic.vo.PageClinicRequest;
import com.tyrael.kharazim.common.dto.DataResponse;
import com.tyrael.kharazim.common.dto.MultiResponse;
import com.tyrael.kharazim.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2023/12/31
 */
@RestController
@RequestMapping("/clinic")
@RequiredArgsConstructor
@Tag(name = "诊所（机构）接口")
public class ClinicController {

    private final ClinicService clinicService;

    @GetMapping("/page")
    @Operation(summary = "诊所（机构）分页")
    public PageResponse<ClinicVO> page(@ParameterObject PageClinicRequest pageRequest) {
        return clinicService.page(pageRequest);
    }

    @GetMapping("/list")
    @Operation(summary = "诊所（机构）列表")
    public MultiResponse<ClinicVO> list(@ParameterObject ListClinicRequest request) {
        return MultiResponse.success(clinicService.list(request));
    }

    @PostMapping("/add")
    @Operation(summary = "新建诊所（机构）", description = "新建诊所（机构），返回诊所（机构）编码")
    public DataResponse<String> add(@RequestBody @Valid AddClinicRequest addClinicRequest) {
        return DataResponse.ok(clinicService.add(addClinicRequest));
    }

}

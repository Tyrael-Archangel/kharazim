package com.tyrael.kharazim.web.controller.clinic;

import com.tyrael.kharazim.application.clinic.service.ClinicService;
import com.tyrael.kharazim.application.clinic.vo.ClinicVO;
import com.tyrael.kharazim.application.clinic.vo.PageClinicRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

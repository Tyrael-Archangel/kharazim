package com.tyrael.kharazim.basicdata.controller.clinic;

import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.base.dto.MultiResponse;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.dto.Response;
import com.tyrael.kharazim.basicdata.app.dto.clinic.*;
import com.tyrael.kharazim.basicdata.app.service.clinic.ClinicService;
import com.tyrael.kharazim.user.api.sdk.annotation.CurrentUser;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
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

    @GetMapping("/export")
    @Operation(summary = "诊所（机构）导出")
    public void export(@ParameterObject PageClinicRequest request,
                       HttpServletResponse httpServletResponse) throws IOException {
        clinicService.export(request, httpServletResponse);
    }

    @GetMapping("/list")
    @Operation(summary = "诊所（机构）列表")
    public MultiResponse<ClinicVO> list(@ParameterObject ListClinicRequest request) {
        return MultiResponse.success(clinicService.list(request));
    }

    @PostMapping("/add")
    @Operation(summary = "新建诊所（机构）", description = "新建诊所（机构），返回诊所（机构）编码")
    public DataResponse<String> add(@RequestBody @Valid AddClinicRequest addClinicRequest) {
        return DataResponse.success(clinicService.add(addClinicRequest));
    }

    @PostMapping("/modify")
    @Operation(summary = "修改诊所（机构）")
    public Response modify(@RequestBody @Valid ModifyClinicRequest modifyClinicRequest,
                           @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        clinicService.modify(modifyClinicRequest, currentUser);
        return Response.success();
    }

}

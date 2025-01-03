package com.tyrael.kharazim.web.controller.customer;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUser;
import com.tyrael.kharazim.application.customer.service.CustomerFamilyService;
import com.tyrael.kharazim.application.customer.vo.family.*;
import com.tyrael.kharazim.common.dto.DataResponse;
import com.tyrael.kharazim.common.dto.MultiResponse;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2024/1/19
 */
@RestController
@RequestMapping("/customer/family")
@RequiredArgsConstructor
@Tag(name = "家庭接口")
public class FamilyController {

    private final CustomerFamilyService customerFamilyService;

    @GetMapping("/{familyCode}")
    @Operation(summary = "查询家庭信息")
    public DataResponse<CustomerFamilyVO> family(
            @PathVariable("familyCode") @Parameter(description = "家庭编码", required = true) String familyCode) {
        return DataResponse.success(customerFamilyService.family(familyCode));
    }

    @GetMapping("/page")
    @Operation(summary = "会员家庭分页")
    public PageResponse<CustomerFamilyVO> page(@ParameterObject PageFamilyRequest pageRequest) {
        return customerFamilyService.page(pageRequest);
    }

    @PostMapping("/create")
    @Operation(summary = "创建家庭", description = "创建家庭，返回家庭编码")
    public DataResponse<String> create(@RequestBody @Valid CreateFamilyRequest createFamilyRequest) {
        return DataResponse.success(customerFamilyService.create(createFamilyRequest));
    }

    @PostMapping("/family-member/add")
    @Operation(summary = "将会员添加进指定的家庭")
    public Response addFamilyMember(@RequestBody @Valid AddFamilyMemberRequest addFamilyMemberRequest) {
        customerFamilyService.addFamilyMember(addFamilyMemberRequest);
        return Response.success();
    }

    @PutMapping("/set-leader/{familyCode}/{customerCode}")
    @Operation(summary = "设置家庭的户主")
    public Response setLeader(@PathVariable("customerCode") String customerCode,
                              @PathVariable("familyCode") String familyCode,
                              @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        customerFamilyService.setLeader(customerCode, familyCode, currentUser);
        return Response.success();
    }

    @PostMapping("/family-member/relation")
    @Operation(summary = "修改会员与户主的关系")
    public Response modifyFamilyMemberRelation(@RequestBody @Valid ModifyFamilyMemberRelationRequest modifyRelationRequest,
                                               @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        customerFamilyService.modifyFamilyMemberRelation(modifyRelationRequest, currentUser);
        return Response.success();
    }

    @PostMapping("/family-member/leave")
    @Operation(summary = "会员退出指定的家庭")
    public Response leaveFamily(@RequestBody @Valid LeaveFamilyRequest leaveFamilyRequest) {
        customerFamilyService.leaveFamily(leaveFamilyRequest);
        return Response.success();
    }

    @GetMapping("/customer/{customerCode}")
    @Operation(summary = "查询会员的家庭信息")
    public MultiResponse<CustomerFamilyVO> customerFamily(
            @PathVariable("customerCode") @Parameter(description = "会员编码", required = true) String customerCode) {
        return MultiResponse.success(customerFamilyService.customerFamily(customerCode));
    }

}

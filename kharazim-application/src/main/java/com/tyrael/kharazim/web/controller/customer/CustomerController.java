package com.tyrael.kharazim.web.controller.customer;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUser;
import com.tyrael.kharazim.application.customer.service.CustomerService;
import com.tyrael.kharazim.application.customer.vo.*;
import com.tyrael.kharazim.common.dto.DataResponse;
import com.tyrael.kharazim.common.dto.MultiResponse;
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
 * @since 2024/1/8
 */
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Tag(name = "会员接口")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{code}")
    @Operation(summary = "查询会员基本信息")
    public DataResponse<CustomerBaseVO> findByCode(
            @PathVariable("code") @Parameter(description = "会员编码", required = true) String code) {
        return DataResponse.ok(customerService.findByCode(code));
    }

    @PostMapping
    @Operation(summary = "新建会员")
    public DataResponse<String> add(@RequestBody @Valid AddCustomerRequest addCustomerRequest,
                                    @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        return DataResponse.ok(customerService.add(addCustomerRequest, currentUser));
    }

    @PostMapping("/{code}")
    @Operation(summary = "修改会员")
    public Response modify(@PathVariable("code") @Parameter(description = "会员编码", required = true) String code,
                           @RequestBody @Valid ModifyCustomerRequest modifyCustomerRequest,
                           @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        customerService.modify(code, modifyCustomerRequest, currentUser);
        return Response.success();
    }

    @PostMapping("/modify-source")
    @Operation(summary = "修改会员的来源会员")
    public Response modifySource(@RequestBody @Valid ModifyCustomerSourceRequest modifySourceRequest,
                                 @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        customerService.modifySource(modifySourceRequest, currentUser);
        return Response.success();
    }

    @PostMapping("/bind-phone")
    @Operation(summary = "会员绑定手机号")
    public Response bindPhone(@RequestBody @Valid BindCustomerPhoneRequest bindCustomerPhoneRequest,
                              @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        customerService.bindPhone(bindCustomerPhoneRequest, currentUser);
        return Response.success();
    }

    @PutMapping("/unbind-phone/{code}")
    @Operation(summary = "会员解绑手机号")
    public Response unbindPhone(@PathVariable("code") @Parameter(description = "会员编码", required = true) String code,
                                @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        customerService.unbindPhone(code, currentUser);
        return Response.success();
    }

    @GetMapping("/list")
    @Operation(summary = "根据指定条件（姓名、电话、证件号码）过滤会员")
    public MultiResponse<CustomerSimpleVO> list(@ParameterObject @Valid ListCustomerRequest request) {
        return MultiResponse.success(customerService.listSimpleInfo(request));
    }

    @GetMapping("/address/{code}")
    @Operation(summary = "查询会员地址")
    public MultiResponse<CustomerAddressVO> addresses(
            @PathVariable("code") @Parameter(description = "会员编码", required = true) String code) {
        return MultiResponse.success(customerService.addresses(code));
    }

    @PostMapping("/address")
    @Operation(summary = "新建会员地址", description = "新建会员地址，返回会员地址ID")
    public DataResponse<Long> addAddress(@RequestBody @Valid AddCustomerAddressRequest addCustomerAddressRequest) {
        return DataResponse.ok(customerService.addAddress(addCustomerAddressRequest));
    }

    @DeleteMapping("/address/{customerCode}/{customerAddressId}")
    @Operation(summary = "删除会员地址")
    public Response deleteCustomerAddress(
            @PathVariable("customerCode") @Parameter(description = "会员编码", required = true) String customerCode,
            @PathVariable("customerAddressId") @Parameter(description = "会员地址ID", required = true) Long customerAddressId) {
        customerService.deleteCustomerAddress(customerCode, customerAddressId);
        return Response.success();
    }

    @PostMapping("/insurance")
    @Operation(summary = "新增会员保险", description = "新增会员保险，返回会员保险ID")
    public DataResponse<Long> addInsurance(
            @RequestBody @Valid AddCustomerInsuranceRequest addCustomerInsuranceRequest) {
        return DataResponse.ok(customerService.addInsurance(addCustomerInsuranceRequest));
    }

    @PutMapping("/service/{customerCode}/{serviceUserCode}")
    @Operation(summary = "设置会员的专属客服")
    public Response assignCustomerServiceUser(
            @PathVariable("customerCode") @Parameter(description = "会员编码", required = true) String customerCode,
            @PathVariable("serviceUserCode") @Parameter(description = "客服人员编码", required = true) String serviceUserCode,
            @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        customerService.assignCustomerServiceUser(customerCode, serviceUserCode, currentUser);
        return Response.success();
    }

    @PutMapping("/sales-consultant/{customerCode}/{salesConsultantCode}")
    @Operation(summary = "设置会员的专属销售顾问")
    public Response assignCustomerSalesConsultant(
            @PathVariable("customerCode") @Parameter(description = "会员编码", required = true) String customerCode,
            @PathVariable("salesConsultantCode") @Parameter(description = "销售顾问编码", required = true) String salesConsultantCode,
            @Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        customerService.assignCustomerSalesConsultant(customerCode, salesConsultantCode, currentUser);
        return Response.success();
    }

}

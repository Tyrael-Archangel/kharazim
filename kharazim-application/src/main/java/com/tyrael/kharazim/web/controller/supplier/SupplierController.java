package com.tyrael.kharazim.web.controller.supplier;

import com.tyrael.kharazim.application.supplier.service.SupplierService;
import com.tyrael.kharazim.application.supplier.vo.AddSupplierRequest;
import com.tyrael.kharazim.application.supplier.vo.ListSupplierRequest;
import com.tyrael.kharazim.application.supplier.vo.PageSupplierRequest;
import com.tyrael.kharazim.application.supplier.vo.SupplierVO;
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
 * @since 2024/2/22
 */
@RestController
@RequestMapping("/supplier")
@RequiredArgsConstructor
@Tag(name = "供应商")
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping("/page")
    @Operation(summary = "供应商分页查询")
    public PageResponse<SupplierVO> page(@ParameterObject PageSupplierRequest pageRequest) {
        return supplierService.page(pageRequest);
    }

    @GetMapping("/list")
    @Operation(summary = "供应商列表数据", description = "供应商列表数据，不分页，返回所有符合条件的数据")
    public MultiResponse<SupplierVO> list(@ParameterObject ListSupplierRequest listRequest) {
        return MultiResponse.success(supplierService.list(listRequest));
    }

    @PostMapping("/add")
    @Operation(summary = "新建供应商", description = "新建供应商，返回供应商编码")
    public DataResponse<String> add(@RequestBody @Valid AddSupplierRequest addSupplierRequest) {
        return DataResponse.ok(supplierService.add(addSupplierRequest));
    }

}

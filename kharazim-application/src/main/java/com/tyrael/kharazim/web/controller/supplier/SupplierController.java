package com.tyrael.kharazim.web.controller.supplier;

import com.tyrael.kharazim.application.supplier.service.SupplierService;
import com.tyrael.kharazim.application.supplier.vo.PageSupplierRequest;
import com.tyrael.kharazim.application.supplier.vo.SupplierVO;
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

}

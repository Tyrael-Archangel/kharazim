package com.tyrael.kharazim.pharmacy.controller;

import com.tyrael.kharazim.base.dto.MultiResponse;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.pharmacy.app.service.InventoryService;
import com.tyrael.kharazim.pharmacy.app.vo.inventory.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Tag(name = "库存管理")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/page")
    @Operation(summary = "库存数据分页")
    public PageResponse<InventoryDTO> page(@ParameterObject PageInventoryRequest pageRequest) {
        return inventoryService.page(pageRequest);
    }

    @GetMapping("/list-clinic")
    @Operation(summary = "查询诊所库存数据")
    public MultiResponse<InventoryDTO> listOfClinic(@ParameterObject ListInventoryOfClinicRequest listRequest) {
        return MultiResponse.success(inventoryService.listOfClinic(listRequest));
    }

    @GetMapping("/page-log")
    @Operation(summary = "库存日志数据分页")
    public PageResponse<InventoryLogVO> pageLog(@ParameterObject PageInventoryLogRequest pageRequest) {
        return inventoryService.pageLog(pageRequest);
    }

    @GetMapping("/page-occupy")
    @Operation(summary = "库存预占数据分页")
    public PageResponse<InventoryOccupyVO> pageOccupy(@ParameterObject @Valid PageInventoryOccupyRequest pageRequest) {
        return inventoryService.pageOccupy(pageRequest);
    }

}

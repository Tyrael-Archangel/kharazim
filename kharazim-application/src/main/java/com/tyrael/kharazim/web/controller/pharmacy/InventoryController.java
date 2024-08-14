package com.tyrael.kharazim.web.controller.pharmacy;

import com.tyrael.kharazim.application.pharmacy.service.InventoryService;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.InventoryVO;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.ListInventoryOfClinicRequest;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.PageInventoryRequest;
import com.tyrael.kharazim.common.dto.MultiResponse;
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
    public PageResponse<InventoryVO> page(@ParameterObject PageInventoryRequest pageRequest) {
        return inventoryService.page(pageRequest);
    }

    @GetMapping("/list-clinic")
    @Operation(summary = "查询诊所库存数据")
    public MultiResponse<InventoryVO> listOfClinic(@ParameterObject ListInventoryOfClinicRequest listRequest) {
        return MultiResponse.success(inventoryService.listOfClinic(listRequest));
    }

}

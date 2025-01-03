package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.application.system.dto.address.AddressDTO;
import com.tyrael.kharazim.application.system.dto.address.AddressTreeNodeDTO;
import com.tyrael.kharazim.application.system.service.AddressQueryService;
import com.tyrael.kharazim.common.dto.DataResponse;
import com.tyrael.kharazim.common.dto.MultiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@RestController
@RequestMapping("/system/address")
@RequiredArgsConstructor
@Tag(name = "行政地址")
public class AddressController {

    private final AddressQueryService addressQueryService;

    @GetMapping("/full-tree")
    @Operation(description = "全国地址树", summary = "全国地址树")
    public MultiResponse<AddressTreeNodeDTO> fullTree() {
        return MultiResponse.success(addressQueryService.fullTree());
    }

    @GetMapping("/tree-by-root/{id}")
    @Operation(description = "某个节点下的地址树", summary = "根据节点ID查询某个节点的地址树")
    public DataResponse<AddressTreeNodeDTO> treeByRootId(@PathVariable("id") Long id) {
        return DataResponse.success(addressQueryService.addressTreeByNodeId(id));
    }

    @GetMapping("/query-by-name/{name}")
    @Operation(description = "根据名称搜索地址", summary = "根据名称搜索地址")
    public MultiResponse<AddressDTO> queryByName(@PathVariable("name") @Parameter(description = "地址名", example = "北京市") String name) {
        return MultiResponse.success(addressQueryService.queryByName(name));
    }

    @GetMapping("/province/list-all")
    @Operation(description = "查询所有省级地址", summary = "查询所有省级地址")
    public MultiResponse<AddressDTO> listProvince() {
        return MultiResponse.success(addressQueryService.listProvince());
    }

    @GetMapping("/cities/{provinceCode}")
    @Operation(description = "查询省份下面的城市", summary = "查询省份下面的城市")
    public MultiResponse<AddressDTO> queryCities(@PathVariable("provinceCode") String provinceCode) {
        return MultiResponse.success(addressQueryService.queryCities(provinceCode));
    }

    @GetMapping("/counties/{cityCode}")
    @Operation(description = "查询城市下面的县（区）", summary = "查询城市下面的县（区）")
    public MultiResponse<AddressDTO> queryCounties(@PathVariable("cityCode") String cityCode) {
        return MultiResponse.success(addressQueryService.queryCounties(cityCode));
    }

}

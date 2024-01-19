package com.tyrael.kharazim.web.controller.customer;

import com.tyrael.kharazim.application.customer.service.CustomerFamilyService;
import com.tyrael.kharazim.application.customer.vo.family.CustomerFamilyVO;
import com.tyrael.kharazim.common.dto.DataResponse;
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
        return DataResponse.ok(customerFamilyService.family(familyCode));
    }

}

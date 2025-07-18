package com.tyrael.kharazim.pos.controller;

import com.tyrael.kharazim.authentication.CurrentPrincipal;
import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.pos.app.ClinicOrderServiceFacade;
import com.tyrael.kharazim.pos.app.vo.CreateClinicOrderCommand;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tyrael Archangel
 * @since 2025/7/15
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/clinic/order")
@Tag(name = "诊所订单")
public class ClinicOrderController {

    private final ClinicOrderServiceFacade clinicOrderService;

    @PostMapping("/create")
    public DataResponse<String> createOrder(@RequestBody @Valid CreateClinicOrderCommand command,
                                            @CurrentPrincipal AuthUser currentUser) {
        return DataResponse.success(clinicOrderService.createOrder(command, currentUser));
    }

}

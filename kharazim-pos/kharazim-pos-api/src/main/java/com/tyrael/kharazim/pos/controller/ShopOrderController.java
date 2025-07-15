package com.tyrael.kharazim.pos.controller;

import com.tyrael.kharazim.pos.app.ShopOrderServiceFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/shop/order")
@Tag(name = "店铺订单")
public class ShopOrderController {

    private final ShopOrderServiceFacade shopOrderService;



}

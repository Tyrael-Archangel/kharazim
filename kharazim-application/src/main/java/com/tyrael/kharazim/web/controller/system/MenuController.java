package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.application.system.dto.menu.SaveMenuRequest;
import com.tyrael.kharazim.application.system.service.MenuService;
import com.tyrael.kharazim.common.dto.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
@Tag(name = "菜单管理")
public class MenuController {

    private final MenuService menuService;

    @Operation(summary = "新增菜单")
    @PostMapping
    public DataResponse<Long> add(@RequestBody @Valid SaveMenuRequest addMenuRequest) {
        return DataResponse.ok(menuService.add(addMenuRequest));
    }
}

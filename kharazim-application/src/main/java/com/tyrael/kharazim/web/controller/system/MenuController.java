package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.application.system.dto.menu.SaveMenuRequest;
import com.tyrael.kharazim.application.system.service.MenuService;
import com.tyrael.kharazim.common.dto.DataResponse;
import com.tyrael.kharazim.common.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping(value = "/{id}")
    @Operation(summary = "修改菜单")
    public Response modify(@Parameter(description = "菜单ID", required = true) @PathVariable Long id,
                           @RequestBody @Valid SaveMenuRequest modifyMenuRequest) {
        menuService.modify(id, modifyMenuRequest);
        return Response.success();
    }

}

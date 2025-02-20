package com.tyrael.kharazim.user.controller;

import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.dto.Response;
import com.tyrael.kharazim.user.app.dto.role.request.SaveRoleRequest;
import com.tyrael.kharazim.user.app.dto.role.response.RoleDetailDTO;
import com.tyrael.kharazim.user.app.dto.role.response.RolePageDTO;
import com.tyrael.kharazim.user.app.dto.user.request.PageRoleRequest;
import com.tyrael.kharazim.user.app.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2024/1/4
 */
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "角色（岗位）管理")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/pages")
    @Operation(description = "角色（岗位）分页", summary = "角色（岗位）分页")
    public PageResponse<RolePageDTO> rolePage(@ParameterObject PageRoleRequest pageRoleRequest) {
        return roleService.rolePage(pageRoleRequest);
    }

    @GetMapping("/{id}")
    @Operation(description = "角色（岗位）详情", summary = "角色（岗位）详情")
    public DataResponse<RoleDetailDTO> roleDetail(@PathVariable("id") Long id) {
        return DataResponse.success(roleService.roleDetail(id));
    }

    @PostMapping
    @Operation(description = "新增角色（岗位）", summary = "新增角色（岗位）")
    public DataResponse<Long> add(@RequestBody @Valid SaveRoleRequest addRoleRequest) {
        return DataResponse.success(roleService.add(addRoleRequest));
    }

    @PutMapping("/{id}")
    @Operation(description = "修改角色（岗位）", summary = "修改角色（岗位）")
    public Response modify(@PathVariable("id") Long id,
                           @RequestBody @Valid SaveRoleRequest modifyRoleRequest) {
        roleService.modify(id, modifyRoleRequest);
        return Response.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    public Response delete(@PathVariable("id") Long id) {
        roleService.delete(id);
        return Response.success();
    }

    @Operation(summary = "禁用角色")
    @PutMapping(value = "/disable/{id}")
    public Response disable(@Schema(description = "角色ID") @PathVariable("id") Long id) {
        roleService.disable(id);
        return Response.success();
    }

    @Operation(summary = "启用角色")
    @PutMapping(value = "/enable/{id}")
    public Response enable(@Schema(description = "角色ID") @PathVariable("id") Long id) {
        roleService.enable(id);
        return Response.success();
    }

}

package com.tyrael.kharazim.web.controller.user;

import com.tyrael.kharazim.application.user.dto.role.response.RolePageDTO;
import com.tyrael.kharazim.application.user.dto.user.request.PageRoleRequest;
import com.tyrael.kharazim.application.user.service.RoleService;
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

}

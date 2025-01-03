package com.tyrael.kharazim.web.controller.user;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUser;
import com.tyrael.kharazim.application.user.dto.user.request.*;
import com.tyrael.kharazim.application.user.dto.user.response.CurrentUserDTO;
import com.tyrael.kharazim.application.user.dto.user.response.UserDTO;
import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import com.tyrael.kharazim.application.user.service.UserService;
import com.tyrael.kharazim.common.dto.DataResponse;
import com.tyrael.kharazim.common.dto.MultiResponse;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2023/12/27
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(description = "根据用户ID获取用户详情", summary = "用户详情")
    public DataResponse<UserDTO> getById(@PathVariable("id") Long id) {
        return DataResponse.success(userService.getById(id));
    }

    @GetMapping("/page")
    @Operation(description = "用户分页数据查询", summary = "用户分页")
    public PageResponse<UserDTO> page(@ParameterObject PageUserRequest pageCommand) {
        return userService.page(pageCommand);
    }

    @GetMapping("/list")
    @Operation(summary = "筛选用户数据")
    public MultiResponse<UserDTO> list(@ParameterObject ListUserRequest listRequest) {
        return MultiResponse.success(userService.list(listRequest));
    }

    @PostMapping("/add")
    @Operation(description = "新增用户，返回用户密码", summary = "新增用户")
    public DataResponse<String> add(@RequestBody @Valid AddUserRequest addUserRequest) {
        return DataResponse.success(userService.add(addUserRequest));
    }

    @PostMapping("/modify")
    @Operation(description = "修改用户", summary = "修改用户")
    public Response modify(@Schema(hidden = true) @CurrentUser AuthUser currentUser,
                           @RequestBody @Valid ModifyUserRequest modifyUserRequest) {
        userService.modify(modifyUserRequest, currentUser);
        return Response.success();
    }

    @PostMapping("/change-password")
    @Operation(description = "修改账户密码", summary = "修改密码")
    public Response changePassword(@Schema(hidden = true) @CurrentUser AuthUser currentUser,
                                   @RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(currentUser, changePasswordRequest);
        return Response.success();
    }

    @PostMapping("/reset-password/{userId}")
    @Operation(description = "重置账户密码，返回新密码", summary = "重置账户密码")
    public DataResponse<String> resetPassword(@Schema(hidden = true) @CurrentUser AuthUser currentUser,
                                              @PathVariable("userId") Long userId) {
        return DataResponse.success(userService.resetPassword(currentUser, userId));
    }

    @GetMapping("/current-user")
    @Operation(description = "获取当前登录用户信息", summary = "获取当前登录用户信息")
    public DataResponse<CurrentUserDTO> currentUser(@Schema(hidden = true) @CurrentUser AuthUser currentUser) {
        return DataResponse.success(userService.getCurrentUserInfo(currentUser));
    }

    @PutMapping("/update-status/{userId}/{status}")
    @Operation(summary = "修改用户状态")
    public Response updateStatus(@PathVariable("userId") Long userId,
                                 @PathVariable("status") EnableStatusEnum status) {
        userService.updateStatus(userId, status);
        return Response.success();
    }

}

package com.tyrael.kharazim.user.controller;

import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.base.dto.MultiResponse;
import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.base.dto.Response;
import com.tyrael.kharazim.base.exception.ForbiddenException;
import com.tyrael.kharazim.user.api.sdk.handler.AuthUserHolder;
import com.tyrael.kharazim.user.app.dto.auth.LoginRequest;
import com.tyrael.kharazim.user.app.dto.auth.OnlineUserDTO;
import com.tyrael.kharazim.user.app.service.AuthService;
import com.tyrael.kharazim.user.sdk.exception.LoginFailedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tyrael Archangel
 * @since 2024/01/03
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "系统认证")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(description = "登录认证，获取token，访问系统其他接口都需要在header头传递ACCESS-TOKEN=获取到的token", summary = "登录认证")
    public DataResponse<String> login(@RequestBody @Valid LoginRequest loginRequest,
                                      HttpServletRequest httpServletRequest) throws LoginFailedException {
        return DataResponse.success(authService.safetyLogin(loginRequest, httpServletRequest));
    }

    @PostMapping("/logout")
    @Operation(description = "退出登录", summary = "退出登录")
    public Response logout(HttpServletRequest httpServletRequest) {
        authService.logout(AuthUserHolder.getCurrentUserToken());
        httpServletRequest.getSession().invalidate();
        return Response.success();
    }

    @GetMapping("/online-users")
    @Operation(summary = "在线用户信息")
    public MultiResponse<OnlineUserDTO> onlineUsers(@ParameterObject PageCommand pageCommand) {
        return MultiResponse.success(authService.onlineUsers(pageCommand));
    }

    @PutMapping("/force-logout")
    @Operation(summary = "强制退出登录")
    public Response forceLogout(@Parameter(description = "token", required = true)
                                @RequestParam("token") String token) {
        if (StringUtils.equals(AuthUserHolder.getCurrentUserToken(), token)) {
            throw new ForbiddenException("can't force logout yourself");
        }
        authService.logout(token);
        return Response.success();
    }

}

package com.tyrael.kharazim.user.controller;

import com.tyrael.kharazim.authentication.Principal;
import com.tyrael.kharazim.authentication.PrincipalHolder;
import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.base.dto.MultiResponse;
import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.base.dto.Response;
import com.tyrael.kharazim.base.exception.ForbiddenException;
import com.tyrael.kharazim.user.app.dto.auth.LoginRequest;
import com.tyrael.kharazim.user.app.dto.auth.OnlineUserDTO;
import com.tyrael.kharazim.user.app.service.AuthService;
import com.tyrael.kharazim.user.sdk.exception.LoginFailedException;
import com.tyrael.kharazim.user.sdk.vo.ClientInfo;
import eu.bitwalker.useragentutils.UserAgent;
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

import java.util.Objects;
import java.util.Optional;

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
        return DataResponse.success(authService.safetyLogin(loginRequest, getClientInfo(httpServletRequest)));
    }

    private ClientInfo getClientInfo(HttpServletRequest request) {

        String host = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(host) || "unknown".equalsIgnoreCase(host)) {
            host = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isBlank(host) || "unknown".equalsIgnoreCase(host)) {
            host = request.getRemoteAddr();
        }
        if (host.contains(",")) {
            host = host.split(",")[0];
        }

        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setHost(host);

        UserAgent userAgent;
        try {
            userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        } catch (Exception e) {
            return clientInfo;
        }

        clientInfo.setBrowser(userAgent.getBrowser().getName());
        clientInfo.setOs(userAgent.getOperatingSystem().getName());
        clientInfo.setBrowserVersion(Objects.toString(userAgent.getBrowserVersion()));
        return clientInfo;
    }

    @PostMapping("/logout")
    @Operation(description = "退出登录", summary = "退出登录")
    public Response logout(HttpServletRequest httpServletRequest) {
        Optional.ofNullable(PrincipalHolder.getPrincipal())
                .map(e -> ((Principal) e).getCode())
                .ifPresent(authService::logout);
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
        String principalToken = Optional.ofNullable(PrincipalHolder.getPrincipal())
                .map(e -> ((Principal) e).getCode())
                .orElse(null);
        if (StringUtils.equals(principalToken, token)) {
            throw new ForbiddenException("can't force logout yourself");
        }
        authService.logout(token);
        return Response.success();
    }

}

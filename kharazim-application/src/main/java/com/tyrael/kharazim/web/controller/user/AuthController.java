package com.tyrael.kharazim.web.controller.user;

import com.tyrael.kharazim.application.base.auth.CurrentUserHolder;
import com.tyrael.kharazim.application.config.cache.CacheKeyConstants;
import com.tyrael.kharazim.application.user.dto.auth.LoginRequest;
import com.tyrael.kharazim.application.user.dto.auth.OnlineUserDTO;
import com.tyrael.kharazim.application.user.service.AuthService;
import com.tyrael.kharazim.common.dto.DataResponse;
import com.tyrael.kharazim.common.dto.MultiResponse;
import com.tyrael.kharazim.common.dto.Response;
import com.tyrael.kharazim.common.exception.LoginFailedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
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
    private final CacheManager cacheManager;

    @PostMapping("/login")
    @Operation(description = "登录认证，获取token，访问系统其他接口都需要在header头传递ACCESS-TOKEN=获取到的token", summary = "登录认证")
    public DataResponse<String> login(@RequestBody @Valid LoginRequest loginRequest,
                                      HttpServletRequest httpServletRequest) throws LoginFailedException {
        return DataResponse.success(authService.safetyLogin(loginRequest, httpServletRequest));
    }

    @PostMapping("/logout")
    @Operation(description = "退出登录", summary = "退出登录")
    public Response logout(HttpServletRequest httpServletRequest) {
        authService.logout(CurrentUserHolder.getCurrentUserToken());
        httpServletRequest.getSession().invalidate();
        Long currentUserId = CurrentUserHolder.getCurrentUserId();
        if (currentUserId != null) {
            Cache currentUserInfoCache = cacheManager.getCache(CacheKeyConstants.CURRENT_USER_INFO);
            if (currentUserInfoCache != null) {
                currentUserInfoCache.evict(currentUserId);
            }
        }
        return Response.success();
    }

    @GetMapping("/online-users")
    @Operation(description = "在线用户信息")
    public MultiResponse<OnlineUserDTO> onlineUsers() {
        return MultiResponse.success(authService.onlineUsers());
    }

}

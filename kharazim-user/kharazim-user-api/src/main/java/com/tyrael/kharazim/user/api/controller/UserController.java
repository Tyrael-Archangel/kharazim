package com.tyrael.kharazim.user.api.controller;

import com.tyrael.kharazim.lib.base.dto.Response;
import com.tyrael.kharazim.lib.base.exception.DomainNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tyrael Archangel
 * @since 2025/2/11
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户接口")
public class UserController {

    @GetMapping("/test")
    public Response aa() {
        throw new DomainNotFoundException("Domain not found");
    }

}

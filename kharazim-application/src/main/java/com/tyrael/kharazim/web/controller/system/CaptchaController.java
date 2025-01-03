package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.application.system.service.CaptchaService;
import com.tyrael.kharazim.common.dto.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
@Tag(name = "验证码接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/captcha")
public class CaptchaController {

    private final CaptchaService captchaService;

    @PostMapping("/sms/{phoneNumber}")
    @Operation(summary = "发送短信验证码", description = "发送短信验证码，返回序列号")
    public DataResponse<String> sendSmsCaptcha(@PathVariable("phoneNumber") String phoneNumber) {
        return DataResponse.success(captchaService.sendSmsCaptcha(phoneNumber));
    }

}
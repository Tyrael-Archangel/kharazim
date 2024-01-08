package com.tyrael.kharazim.application.system.service;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
public interface CaptchaService {

    /**
     * 发送短信验证码
     *
     * @param phoneNumber 手机号
     * @return 短信序列号
     */
    String sendSmsCaptcha(String phoneNumber);

    /**
     * 校验短信验证码
     *
     * @param captcha    验证码
     * @param serialCode 短信序列号
     */
    void verifySmsCaptcha(String captcha, String serialCode);

}

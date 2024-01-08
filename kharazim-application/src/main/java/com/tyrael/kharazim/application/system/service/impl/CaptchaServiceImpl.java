package com.tyrael.kharazim.application.system.service.impl;

import com.tyrael.kharazim.application.config.CaptchaConfig;
import com.tyrael.kharazim.application.system.service.CaptchaService;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.util.RandomStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private final CaptchaConfig captchaConfig;
    private final StringRedisTemplate redisTemplate;

    @Override
    public String sendSmsCaptcha(String phoneNumber) {
        String trimmedPhoneNumber = StringUtils.trim(phoneNumber);

        // 验证手机号
        verifyPhoneNumber(trimmedPhoneNumber);

        String captcha = RandomStringUtil.makeNumber(captchaConfig.getCaptchaLength());
        String serialCode = RandomStringUtil.make();

        // 保存验证码
        saveCaptchaCacheCache(serialCode, trimmedPhoneNumber, captcha);

        try {
            // 发送验证码短信
            sendCaptchaSms(captcha, trimmedPhoneNumber);
        } catch (Exception e) {
            clearCaptchaCacheCache(serialCode, trimmedPhoneNumber);
            throw e;
        }

        return serialCode;
    }

    private void verifyPhoneNumber(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            throw new BusinessException("手机号码格式有误");
        }
        String phoneNumberRegex = "^(\\+86)?(1[3-9])\\d{9}$";
        if (!phoneNumber.matches(phoneNumberRegex)) {
            throw new BusinessException("手机号码格式有误");
        }
    }

    private void saveCaptchaCacheCache(String serialCode, String phoneNumber, String captcha) {

        String captchaCacheKey = captchaCacheKey(serialCode);
        String phoneCaptchaCacheKey = phoneCaptchaCacheKey(phoneNumber);
        String cacheValue = new PhoneCaptcha(phoneNumber, captcha, serialCode).toCacheValue();

        Duration ttl = Duration.ofMinutes(captchaConfig.getIntervalMinutes());
        Boolean savePhoneSuccess = redisTemplate.opsForValue()
                .setIfAbsent(phoneCaptchaCacheKey, cacheValue, ttl);
        if (!Boolean.TRUE.equals(savePhoneSuccess)) {
            throw new BusinessException("获取短信验证码过于频繁，请稍后再试");
        }

        redisTemplate.opsForValue()
                .set(captchaCacheKey, cacheValue, ttl);
    }

    private void clearCaptchaCacheCache(String serialCode, String phone) {
        String captchaCacheKey = captchaCacheKey(serialCode);
        String phoneCaptchaCacheKey = phoneCaptchaCacheKey(phone);
        redisTemplate.delete(Arrays.asList(captchaCacheKey, phoneCaptchaCacheKey));
    }

    private String captchaCacheKey(String serialCode) {
        return captchaConfig.getCaptchaCodePrefix() + ":" + serialCode;
    }

    private String phoneCaptchaCacheKey(String phone) {
        return captchaConfig.getCaptchaCodePrefix() + ":PHONE:" + phone;
    }

    private void sendCaptchaSms(String captcha, String trimmedPhoneNumber) {
        // 发送短信验证码
        log.info("send captcha [" + captcha + "] to " + trimmedPhoneNumber);
    }

    @Override
    public void verifySmsCaptcha(String captcha, String serialCode) {
        String captchaCacheKey = this.captchaCacheKey(serialCode);
        String captchaCacheValue = redisTemplate.opsForValue().get(captchaCacheKey);
        if (StringUtils.isBlank(captchaCacheValue)) {
            throw new BusinessException("验证码已失效");
        }

        PhoneCaptcha phoneCaptcha = PhoneCaptcha.parse(captchaCacheValue);
        if (StringUtils.equalsIgnoreCase(captcha, phoneCaptcha.captcha())) {
            clearCaptchaCacheCache(serialCode, phoneCaptcha.phone());
        } else {
            throw new BusinessException("验证码有误");
        }
    }

    private record PhoneCaptcha(String phone, String captcha, String serialCode) {

        private static final String sep = "__";

        public static PhoneCaptcha parse(String cacheValue) {
            String[] split = cacheValue.split(sep);
            try {
                return new PhoneCaptcha(split[0], split[1], split[2]);
            } catch (Exception e) {
                throw new BusinessException("系统内部错误，验证码数据异常");
            }
        }

        public String toCacheValue() {
            return phone + sep + captcha + sep + serialCode;
        }

    }

}

package com.tyrael.kharazim.application.system.service.impl;


import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.system.service.CodeGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
public abstract class AbstractCodeGenerator implements CodeGenerator {

    protected final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    protected final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 获取指定tag的自增
     *
     * @param tag 指定tag
     * @return 自增long
     */
    abstract long increment(String tag);

    @Override
    public String next(BusinessCodeConstants businessCode) {
        String next = this.next(businessCode.name(), businessCode.getBit());
        String prefix = businessCode.getPrefix();
        return prefix == null ? next : (prefix.trim() + next);
    }

    @Override
    public String next(String tag, int bit) {
        long nextValue = increment(tag);
        return format(nextValue, bit);
    }

    @Override
    public String dailyNext(BusinessCodeConstants businessCode) {
        String dayTime = dateFormatter.format(LocalDate.now());
        long nextValue = increment(businessCode.name() + dayTime);
        String dailyNext = dayTime + format(nextValue, businessCode.getBit());
        return joinPrefix(businessCode, dailyNext);
    }

    @Override
    public String dailyTimeNext(BusinessCodeConstants businessCode) {
        LocalDateTime now = LocalDateTime.now();
        long nextValue = increment(businessCode.name() + dateFormatter.format(now.toLocalDate()));
        String dailyTimeNext = dateTimeFormatter.format(now) + format(nextValue, businessCode.getBit());
        return joinPrefix(businessCode, dailyTimeNext);
    }

    private String joinPrefix(BusinessCodeConstants businessCode, String code) {
        String prefix = businessCode.getPrefix();
        return prefix == null ? code : (prefix.trim() + code);
    }

    private String format(long value, int bit) {
        String s = Long.toString(value);
        int zeroCount = bit - s.length();
        if (zeroCount <= 0) {
            return s;
        }
        return "0".repeat(zeroCount) + s;
    }

}

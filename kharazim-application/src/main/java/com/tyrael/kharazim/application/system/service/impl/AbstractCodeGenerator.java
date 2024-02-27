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
        String dailyNext = dailyNext(businessCode.name(), businessCode.getBit());
        String prefix = businessCode.getPrefix();
        return prefix == null ? dailyNext : (prefix.trim() + dailyNext);
    }

    private String dailyNext(String tag, int bit) {
        String dayTime = dateFormatter.format(LocalDate.now());
        long nextValue = increment(tag + dayTime);
        return dayTime + format(nextValue, bit);
    }

    @Override
    public String dailyTimeNext(BusinessCodeConstants businessCode) {
        String dailyTimeNext = dailyTimeNext(businessCode.name(), businessCode.getBit());
        String prefix = businessCode.getPrefix();
        return prefix == null ? dailyTimeNext : (prefix.trim() + dailyTimeNext);
    }

    private String dailyTimeNext(String tag, int bit) {
        LocalDateTime now = LocalDateTime.now();
        long nextValue = increment(tag + dateFormatter.format(now.toLocalDate()));
        return dateTimeFormatter.format(now) + format(nextValue, bit);
    }

    protected String format(long value, int bit) {
        String s = Long.toString(value);
        int zeroCount = bit - s.length();
        if (zeroCount <= 0) {
            return s;
        }
        return "0".repeat(zeroCount) + s;
    }

}

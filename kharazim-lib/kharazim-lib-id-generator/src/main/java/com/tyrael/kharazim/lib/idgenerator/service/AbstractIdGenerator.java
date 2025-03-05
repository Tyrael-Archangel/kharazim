package com.tyrael.kharazim.lib.idgenerator.service;

import com.tyrael.kharazim.lib.idgenerator.BusinessIdConstant;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
public abstract class AbstractIdGenerator implements IdGenerator {

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
    public <E extends Enum<E> & BusinessIdConstant<E>> String next(E businessCode) {
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
    public <E extends Enum<E> & BusinessIdConstant<E>> String dailyNext(E businessCode) {
        String dayTime = dateFormatter.format(LocalDate.now());
        long nextValue = increment(businessCode.name() + dayTime);
        String dailyNext = dayTime + format(nextValue, businessCode.getBit());
        return joinPrefix(businessCode, dailyNext);
    }

    @Override
    public <E extends Enum<E> & BusinessIdConstant<E>> String dailyTimeNext(E businessCode) {
        LocalDateTime now = LocalDateTime.now();
        long nextValue = increment(businessCode.name() + dateFormatter.format(now.toLocalDate()));
        String dailyTimeNext = dateTimeFormatter.format(now) + format(nextValue, businessCode.getBit());
        return joinPrefix(businessCode, dailyTimeNext);
    }

    private <E extends Enum<E> & BusinessIdConstant<E>> String joinPrefix(BusinessIdConstant<E> businessCode, String code) {
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

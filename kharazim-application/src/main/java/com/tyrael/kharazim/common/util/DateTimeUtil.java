package com.tyrael.kharazim.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * @author Tyrael Archangel
 * @since 2024/3/28
 */
public class DateTimeUtil {

    public static LocalDateTime startTimeOfDate(LocalDate date) {
        return date == null ? null : date.atStartOfDay();
    }

    public static LocalDateTime endTimeOfDate(LocalDate date) {
        return date == null ? null : (date.plusDays(1).atStartOfDay().minusSeconds(1));
    }

    public static LocalDateTime startTimeOfMonth(YearMonth yearMonth) {
        return yearMonth == null ? null : startTimeOfDate(yearMonth.atDay(1));
    }

    public static LocalDateTime endTimeOfMonth(YearMonth yearMonth) {
        return yearMonth == null ? null : endTimeOfDate(yearMonth.atEndOfMonth());
    }

}

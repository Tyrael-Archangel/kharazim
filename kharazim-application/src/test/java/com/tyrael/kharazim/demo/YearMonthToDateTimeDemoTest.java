package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * @author Tyrael Archangel
 * @since 2024/3/19
 */
public class YearMonthToDateTimeDemoTest {

    @Test
    public void yearMonthToDateTime() {
        YearMonth yearMonth = YearMonth.now();
        System.out.println(startTimeOfMonth(yearMonth));
        System.out.println(endTimeOfMonth(yearMonth));

        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        System.out.println(startTimeOfMonth(lastMonth));
        System.out.println(endTimeOfMonth(lastMonth));
    }

    public LocalDateTime startTimeOfDate(LocalDate date) {
        return date == null ? null : date.atStartOfDay();
    }

    public LocalDateTime endTimeOfDate(LocalDate date) {
        return date == null ? null : (date.plusDays(1).atStartOfDay().minusSeconds(1));
    }

    public LocalDateTime startTimeOfMonth(YearMonth yearMonth) {
        return yearMonth == null ? null : startTimeOfDate(yearMonth.atDay(1));
    }

    public LocalDateTime endTimeOfMonth(YearMonth yearMonth) {
        return yearMonth == null ? null : endTimeOfDate(yearMonth.atEndOfMonth());
    }

}

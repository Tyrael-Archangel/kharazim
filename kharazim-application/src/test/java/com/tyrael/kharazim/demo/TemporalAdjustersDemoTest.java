package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * @author Tyrael Archangel
 * @since 2024/2/20
 */
public class TemporalAdjustersDemoTest {

    @Test
    public void temporalAdjusters() {

        LocalDate today = LocalDate.now();
        System.out.println("今天: " + today);

        // 本周第一天
        LocalDate startDayOfCurrentWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        System.out.println("本周第一天: " + startDayOfCurrentWeek);

        // 本周最后一天
        LocalDate endDayOfCurrentWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        System.out.println("本周最后一天: " + endDayOfCurrentWeek);

        // 本月第一天
        LocalDate firstDayOfCurrentMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        System.out.println("本月第一天: " + firstDayOfCurrentMonth);

        // 本月最后一天
        LocalDate lastDayOfCurrentMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println("本月最后一天: " + lastDayOfCurrentMonth);

        // 本月第一个星期六
        LocalDate firstSundayOfCurrentMonth = today.with(TemporalAdjusters.firstInMonth(DayOfWeek.SATURDAY));
        System.out.println("本月第一个星期六: " + firstSundayOfCurrentMonth);

        // 本月最后一个星期三
        LocalDate lastSundayOfCurrentMonth = today.with(TemporalAdjusters.lastInMonth(DayOfWeek.WEDNESDAY));
        System.out.println("本月最后一个星期三: " + lastSundayOfCurrentMonth);

        // 本月的第三个星期三
        LocalDate thirdWednesdayOfCurrentMonth = today.with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.WEDNESDAY));
        System.out.println("本月的第三个星期三: " + thirdWednesdayOfCurrentMonth);

    }

}

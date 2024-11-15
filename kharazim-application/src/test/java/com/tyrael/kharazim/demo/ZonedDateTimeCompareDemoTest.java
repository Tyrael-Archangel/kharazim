package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/11/15
 */
public class ZonedDateTimeCompareDemoTest {

    @Test
    public void zonedDateTimeCompare() {

        ZonedDateTime utc = ZonedDateTime.parse("2024-11-15T00:00:00Z");
        ZonedDateTime beijing = ZonedDateTime.parse("2024-11-15T08:00:00+08:00");
        System.out.println(utc);
        System.out.println(beijing);

        System.out.println(utc.toInstant().equals(beijing.toInstant()));

        System.out.println(utc.isBefore(beijing));
        System.out.println(utc.isAfter(beijing));

        System.out.println(beijing.isBefore(utc));
        System.out.println(beijing.isAfter(utc));
    }

}

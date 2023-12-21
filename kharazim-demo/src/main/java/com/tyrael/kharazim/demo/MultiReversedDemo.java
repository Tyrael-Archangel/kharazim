package com.tyrael.kharazim.demo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
public class MultiReversedDemo {

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        List<SortDTO> list = List.of(
                SortDTO.builder()
                        .code("TEST0001")
                        .time(now.minusHours(5))
                        .quantity(BigDecimal.valueOf(3))
                        .build(),
                SortDTO.builder()
                        .code("TEST0002")
                        .time(now.minusHours(1))
                        .quantity(BigDecimal.valueOf(1))
                        .build());

        list = list.stream()
                .sorted((Comparator.comparing(SortDTO::getQuantity).reversed())
                        .thenComparing(Comparator.comparing(SortDTO::getTime).reversed())
                        .thenComparing(Comparator.comparing(SortDTO::getCode).reversed()))
                .toList();
        list.forEach(System.out::println);
    }

    @Data
    @Builder
    private static class SortDTO {

        private String code;
        private LocalDateTime time;
        private BigDecimal quantity;

    }

}

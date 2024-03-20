package com.tyrael.kharazim.demo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/3/20
 */
public class NullCompareDemo {

    public static void main(String[] args) {

        List<Record> records = List.of(
                new Record(1, "a"),
                new Record(null, "a"),
                new Record(null, "b"),
                new Record(2, "a")
        );

        List<Record> sorted = records.stream()
                .sorted(Comparator.comparing(Record::i, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Comparator.comparing(Record::s).reversed()))
                .collect(Collectors.toList());
        System.out.println(sorted);
    }

    private record Record(Integer i, String s) {
    }

}

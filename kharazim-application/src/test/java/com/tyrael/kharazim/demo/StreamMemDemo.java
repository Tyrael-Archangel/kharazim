package com.tyrael.kharazim.demo;

import java.util.stream.Stream;

/**
 * -Xmx300m -Xms300m
 *
 * @author Tyrael Archangel
 * @since 2024/10/23
 */
public class StreamMemDemo {

    public static void main(String[] args) {
        Stream<Item> stream = Stream.iterate(
                new Item(0, new byte[1024 * 1024]),
                x -> x.index() < 10000,
                x -> new Item(x.index() + 1, new byte[1024 * 1024])
        );

        stream.forEach(System.out::println);
    }

    record Item(int index, byte[] bytes) {
    }

}


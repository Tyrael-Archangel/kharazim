package com.tyrael.kharazim.common.dto;

/**
 * @param left  Left value
 * @param right Right value
 * @param <L>   Left value type
 * @param <R>   Right value type
 * @author Tyrael Archangel
 * @since 2024/1/18
 */
public record Pair<L, R>(L left, R right) {

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }

}

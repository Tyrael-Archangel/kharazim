package com.tyrael.kharazim.base.dto;

import java.util.ArrayList;

/**
 * @author Tyrael Archangel
 * @since 2024/1/18
 */
public class Pairs<L, R> extends ArrayList<Pair<L, R>> {

    public Pairs<L, R> append(L left, R right) {
        this.add(Pair.of(left, right));
        return this;
    }

}

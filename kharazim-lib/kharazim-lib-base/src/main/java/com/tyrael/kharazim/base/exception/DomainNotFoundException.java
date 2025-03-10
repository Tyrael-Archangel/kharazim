package com.tyrael.kharazim.base.exception;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author Tyrael Archangel
 * @since 2023/12/22
 */
@Getter
public class DomainNotFoundException extends RuntimeException {

    private final Serializable searchWord;

    public DomainNotFoundException(Serializable searchWord) {
        super("Not found: " + searchWord);
        this.searchWord = searchWord;
    }

    public static <T> void assertFound(T t, Serializable searchWord) {
        if (t == null) {
            throw new DomainNotFoundException(searchWord);
        }
    }

}

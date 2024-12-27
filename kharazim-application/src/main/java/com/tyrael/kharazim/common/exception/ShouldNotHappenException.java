package com.tyrael.kharazim.common.exception;

/**
 * @author Tyrael Archangel
 * @since 2023/12/22
 */
public class ShouldNotHappenException extends RuntimeException {

    public ShouldNotHappenException() {
    }

    public ShouldNotHappenException(String message) {
        super(message);
    }

    public ShouldNotHappenException(Throwable cause) {
        super(cause);
    }

}

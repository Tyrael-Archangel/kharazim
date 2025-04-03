package com.tyrael.kharazim.mq;

/**
 * @author Tyrael Archangel
 * @since 2025/4/2
 */
public class MessageSerializeException extends RuntimeException {

    public MessageSerializeException(String message) {
        super(message);
    }

    public MessageSerializeException(Throwable cause) {
        super(cause);
    }

}

package com.tyrael.kharazim.mq;

/**
 * @author Tyrael Archangel
 * @since 2025/3/31
 */
public interface MqConsumer<T> {

    /**
     * topic
     *
     * @return topic
     */
    String getTopic();

    /**
     * consume
     *
     * @param message message
     */
    default void consume(Message<T> message) {
        this.consume(message.getBody());
    }

    /**
     * consume
     *
     * @param body message body
     */
    void consume(T body);

}

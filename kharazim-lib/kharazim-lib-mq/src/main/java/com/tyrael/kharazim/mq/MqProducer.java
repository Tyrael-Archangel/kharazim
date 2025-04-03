package com.tyrael.kharazim.mq;

/**
 * @author Tyrael Archangel
 * @since 2025/3/31
 */
public interface MqProducer {

    /**
     * send message
     *
     * @param topic       topic
     * @param messageBody messageBody
     * @param <T>         type of messageBody
     */
    default <T> void send(String topic, T messageBody) {
        this.send(new Message<>(topic, messageBody));
    }

    /**
     * send msg
     *
     * @param message msg
     */
    <T> void send(Message<T> message);

}

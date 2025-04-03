package com.tyrael.kharazim.mq;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author Tyrael Archangel
 * @since 2025/3/31
 */
@Data
@NoArgsConstructor
public class Message<T> {

    private String id;
    private String topic;
    private T body;
    private long timestamp;

    public Message(String topic, T body) {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        this.topic = topic;
        this.body = body;
        this.timestamp = System.currentTimeMillis();
    }

}

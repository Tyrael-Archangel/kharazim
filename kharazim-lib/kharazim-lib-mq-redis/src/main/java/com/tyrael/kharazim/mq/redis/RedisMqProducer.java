package com.tyrael.kharazim.mq.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyrael.kharazim.mq.Message;
import com.tyrael.kharazim.mq.MessageSerializeException;
import com.tyrael.kharazim.mq.MqProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyrael Archangel
 * @since 2025/3/31
 */
@RequiredArgsConstructor
public class RedisMqProducer implements MqProducer {

    public static final String MESSAGE_BODY_KEY = "messageBody";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public <T> void send(Message<T> message) {

        String messageBody;
        try {
            messageBody = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new MessageSerializeException(e);
        }

        Map<String, String> streamMessage = new HashMap<>();
        streamMessage.put(MESSAGE_BODY_KEY, messageBody);
        redisTemplate.opsForStream()
                .add(StreamRecords.newRecord().ofObject(streamMessage).withStreamKey(message.getTopic()));
    }

}

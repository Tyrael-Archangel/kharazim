package com.tyrael.kharazim.mq.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyrael.kharazim.mq.Message;
import com.tyrael.kharazim.mq.MessageSerializeException;
import com.tyrael.kharazim.mq.MqProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
    private final String topicPrefix;

    @Override
    public <T> void send(Message<T> message) {

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    sendMessage(message);
                }
            });

        } else {
            sendMessage(message);
        }

    }

    private <T> void sendMessage(Message<T> message) {
        String messageBody;
        try {
            messageBody = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new MessageSerializeException(e);
        }

        ObjectRecord<String, Map<String, String>> record = StreamRecords.newRecord()
                .ofObject(Map.of(MESSAGE_BODY_KEY, messageBody))
                .withStreamKey(topicPrefix + message.getTopic());
        redisTemplate.opsForStream().add(record);
    }

}

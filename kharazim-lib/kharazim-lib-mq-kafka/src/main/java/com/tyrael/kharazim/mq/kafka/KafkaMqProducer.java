package com.tyrael.kharazim.mq.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyrael.kharazim.mq.Message;
import com.tyrael.kharazim.mq.MqProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author Tyrael Archangel
 * @since 2025/4/3
 */
@RequiredArgsConstructor
public class KafkaMqProducer implements MqProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public <T> void send(Message<T> message) {
        String messageBody;
        try {
            messageBody = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaTemplate.send(message.getTopic(), message.getId(), messageBody);
    }

}

package com.tyrael.kharazim.mq.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyrael.kharazim.mq.Message;
import com.tyrael.kharazim.mq.MqProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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

        String topic = message.getTopic();
        String messageId = message.getId();
        String messageBody;
        try {
            messageBody = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    kafkaTemplate.send(topic, messageId, messageBody);
                }
            });

        } else {
            kafkaTemplate.send(topic, messageId, messageBody);
        }

    }

}

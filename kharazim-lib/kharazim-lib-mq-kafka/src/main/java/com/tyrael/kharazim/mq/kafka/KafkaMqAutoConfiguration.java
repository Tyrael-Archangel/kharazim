package com.tyrael.kharazim.mq.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyrael.kharazim.mq.MqConsumer;
import com.tyrael.kharazim.mq.MqConsumerWrapper;
import com.tyrael.kharazim.mq.MqProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;

import java.util.List;
import java.util.Optional;

/**
 * @author Tyrael Archangel
 * @since 2025/4/2
 */
@Configuration
@ConditionalOnProperty(value = "mq.kafka.enable", havingValue = "true", matchIfMissing = true)
public class KafkaMqAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MqProducer.class)
    public MqProducer mqProducer(KafkaTemplate<String, String> kafkaTemplate,
                                 ObjectMapper objectMapper) {
        return new KafkaMqProducer(kafkaTemplate, objectMapper);
    }

    @Bean
    public KafkaMqConsumerStarter kafkaMqConsumerStarter(ConsumerFactory<String, String> consumerFactory,
                                                         KafkaProperties kafkaProperties,
                                                         List<MqConsumer<?>> consumers,
                                                         ObjectMapper objectMapper) {
        return new KafkaMqConsumerStarter(consumerFactory, kafkaProperties, consumers, objectMapper);
    }

    @RequiredArgsConstructor
    public static class KafkaMqConsumerStarter {

        private final ConsumerFactory<String, String> consumerFactory;
        private final KafkaProperties kafkaProperties;
        private final List<MqConsumer<?>> consumers;
        private final ObjectMapper objectMapper;

        @EventListener(ApplicationReadyEvent.class)
        public void start() {
            for (MqConsumer<?> consumer : consumers) {
                ContainerProperties containerProperties = new ContainerProperties(consumer.getTopic());
                containerProperties.setMessageListener(messageListener(consumer, kafkaProperties, objectMapper));
                ConcurrentMessageListenerContainer<String, String> container = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
                container.start();
            }
        }

        private MessageListener<String, String> messageListener(MqConsumer<?> consumer,
                                                                KafkaProperties kafkaProperties,
                                                                ObjectMapper objectMapper) {
            MqConsumerWrapper<?> mqConsumerWrapper = new MqConsumerWrapper<>(consumer, objectMapper);
            boolean enableAutoCommit = Optional.ofNullable(kafkaProperties)
                    .map(KafkaProperties::getConsumer)
                    .map(KafkaProperties.Consumer::getEnableAutoCommit)
                    .orElse(false);

            if (enableAutoCommit) {
                return data -> mqConsumerWrapper.doConsume(data.value());
            } else {
                return (AcknowledgingMessageListener<String, String>) (data, acknowledgment) -> {
                    mqConsumerWrapper.doConsume(data.value());
                    if (acknowledgment != null) {
                        acknowledgment.acknowledge();
                    }
                };
            }

        }
    }

}

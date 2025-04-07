package com.tyrael.kharazim.mq.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyrael.kharazim.mq.MqConsumer;
import com.tyrael.kharazim.mq.MqConsumerWrapper;
import com.tyrael.kharazim.mq.MqProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.tyrael.kharazim.mq.redis.RedisMqProducer.MESSAGE_BODY_KEY;

/**
 * @author Tyrael Archangel
 * @since 2025/4/2
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "mq.redis.enable", havingValue = "true", matchIfMissing = true)
public class RedisMqAutoConfiguration {

    private final String defaultTopicPrefix = "MQ_TOPIC:";

    @Bean
    @ConditionalOnMissingBean(MqProducer.class)
    public MqProducer mqProducer(StringRedisTemplate redisTemplate,
                                 ObjectMapper objectMapper,
                                 @Value("${mq.redis.topic-prefix:" + defaultTopicPrefix + "}") String topicPrefix) {
        return new RedisMqProducer(redisTemplate, objectMapper, topicPrefix);
    }

    @Bean
    @ConditionalOnBean(MqConsumer.class)
    public RedisMqConsumerContainer redisMqConsumerContainer(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            @Value("${spring.application.name:}") String consumerGroup,
            @Value("${server.port:}") Integer serverPort,
            @Value("${mq.redis.topic-prefix:" + defaultTopicPrefix + "}") String topicPrefix,
            List<MqConsumer<?>> mqConsumers) {
        String hostName;
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            hostName = localHost.getHostName();
        } catch (UnknownHostException e) {
            hostName = "unknown";
        }
        String consumerName = consumerGroup + "-" + hostName + "-" + serverPort;
        return new RedisMqConsumerContainer(redisTemplate, objectMapper, topicPrefix, mqConsumers, consumerGroup, consumerName);
    }

    public static class RedisMqConsumerContainer {

        private final AtomicInteger threadId = new AtomicInteger(0);
        private final StringRedisTemplate redisTemplate;
        private final String topicPrefix;
        private final String consumerGroup;
        private final String consumerName;
        private final Map<String, MqConsumerWrapper<?>> consumerMap;

        private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5,
                5,
                30,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                r -> new Thread(r, "t-mq-consumer-" + threadId.incrementAndGet()),
                new ThreadPoolExecutor.CallerRunsPolicy());
        private volatile boolean running = true;

        public RedisMqConsumerContainer(StringRedisTemplate redisTemplate,
                                        ObjectMapper objectMapper,
                                        String topicPrefix,
                                        List<MqConsumer<?>> mqConsumers,
                                        String consumerGroup,
                                        String consumerName) {
            this.redisTemplate = redisTemplate;
            this.topicPrefix = topicPrefix;
            this.consumerGroup = consumerGroup;
            this.consumerName = consumerName;
            this.consumerMap = mqConsumers.stream()
                    .collect(Collectors.toMap(MqConsumer::getTopic, e -> new MqConsumerWrapper<>(e, objectMapper)));
        }

        @EventListener(ApplicationReadyEvent.class)
        public void start() {

            log.info("Start Redis consumer");

            @SuppressWarnings({"unchecked"})
            StreamOffset<String>[] streamOffsets = consumerMap.keySet()
                    .stream()
                    .map(topic -> StreamOffset.create(topicPrefix + topic, ReadOffset.lastConsumed()))
                    .toList()
                    .toArray(new StreamOffset[0]);

            new Thread(() -> {

                for (String topic : consumerMap.keySet()) {
                    autoCreateTopicAndGroup(topicPrefix + topic, consumerGroup);
                }

                while (running) {

                    List<MapRecord<String, String, String>> messages = null;
                    try {
                        messages = redisTemplate.<String, String>opsForStream().read(
                                Consumer.from(consumerGroup, consumerName),
                                StreamReadOptions.empty().count(20).block(Duration.ofSeconds(10)),
                                streamOffsets);
                    } catch (RedisSystemException e) {
                        if (running) {
                            throw e;
                        }
                    }

                    if (messages != null && !messages.isEmpty()) {

                        List<Future<?>> futures = new ArrayList<>();
                        Map<String, List<RecordId>> topicRecords = new LinkedHashMap<>();

                        for (MapRecord<String, String, String> message : messages) {
                            String topic = message.getRequiredStream();
                            String messageBody = message.getValue().get(MESSAGE_BODY_KEY);
                            MqConsumerWrapper<?> mqConsumerWrapper = consumerMap.get(topic);

                            topicRecords.computeIfAbsent(topic, k -> new ArrayList<>())
                                    .add(message.getId());
                            futures.add(threadPoolExecutor.submit(() -> mqConsumerWrapper.doConsume(messageBody)));
                        }

                        for (Future<?> future : futures) {
                            try {
                                future.get();
                            } catch (InterruptedException | ExecutionException e) {
                                log.error("Error while consuming message", e);
                            }
                        }

                        topicRecords.forEach((topic, recordIds) ->
                                redisTemplate.opsForStream()
                                        .acknowledge(topic, consumerGroup, recordIds.toArray(new RecordId[0]))
                        );

                    }
                }

            }, "T-consumer-fetcher").start();
        }

        private void autoCreateTopicAndGroup(String topic, String group) {

            Boolean keyExist = redisTemplate.hasKey(topic);
            if (!Boolean.TRUE.equals(keyExist)) {
                redisTemplate.opsForStream()
                        .createGroup(topic, group);
            }
            boolean groupExist = redisTemplate.opsForStream()
                    .groups(topic)
                    .stream()
                    .map(StreamInfo.XInfoGroup::groupName)
                    .anyMatch(group::equalsIgnoreCase);
            if (!groupExist) {
                redisTemplate.opsForStream()
                        .createGroup(topic, group);
            }
        }

        @EventListener(ContextClosedEvent.class)
        public void stop() {
            this.running = false;
            log.warn("Stop Redis consumer");
        }

    }
}

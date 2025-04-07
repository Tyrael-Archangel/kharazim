package com.tyrael.kharazim.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/4/3
 */
public class MqConsumerWrapper<T> {

    private final MqConsumer<T> consumer;
    private final Class<T> messageBodyType;
    private final ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    public MqConsumerWrapper(MqConsumer<T> consumer, ObjectMapper objectMapper) {
        this.consumer = consumer;
        this.messageBodyType = (Class<T>) getConsumerBodyType(consumer.getClass());
        this.objectMapper = objectMapper;
        if (this.messageBodyType == null) {
            throw new MessageSerializeException("Cannot resolve MqConsumer message body type: " + consumer.getClass());
        }
    }

    private Class<?> getConsumerBodyType(Type type) {
        if (type instanceof Class<?> clazz) {

            List<Type> supers = new ArrayList<>(List.of(clazz.getGenericInterfaces()));
            supers.add(clazz.getGenericSuperclass());

            for (Type superType : supers) {
                if (superType instanceof ParameterizedType parameterizedType) {
                    Type rawType = parameterizedType.getRawType();
                    if (rawType.equals(MqConsumer.class)) {
                        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    }
                }
                Class<?> consumerBodyType = getConsumerBodyType(superType);
                if (consumerBodyType != null) {
                    return consumerBodyType;
                }
            }
        }

        return null;
    }

    public void doConsume(String messageBody) {
        Message<T> message;
        try {
            JavaType type = objectMapper.getTypeFactory()
                    .constructParametricType(Message.class, this.messageBodyType);
            message = objectMapper.readValue(messageBody, type);
        } catch (JsonProcessingException e) {
            throw new MessageSerializeException(e);
        }
        consumer.consume(message);
    }

}

package com.tyrael.kharazim.lib.web.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tyrael.kharazim.base.dto.BaseHasNameEnum;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
@Configuration(proxyBeanMethods = false)
public class JacksonConfig {

    private static final String DEFAULT_LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public JavaTimeModule javaTimeModule(JacksonProperties jacksonProperties) {
        String dateFormat = jacksonProperties.getDateFormat();
        if (!StringUtils.hasText(dateFormat)) {
            dateFormat = DEFAULT_LOCAL_DATE_TIME_FORMAT;
        }

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeI18nSerializer(dateFormat));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeI18nDeserializer(dateFormat));

        return javaTimeModule;
    }

    @Bean
    @Primary
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        DefaultDeserializationContext dc = new DefaultDeserializationContext.Impl(EnumBeanDeserializerFactory.INSTANCE);
        ObjectMapper objectMapper = new ObjectMapper(null, null, dc);
        builder.configure(objectMapper);
        return objectMapper;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer baseNameAndValueEnumJsonCustomizer() {
        BaseNameAndValueEnumSerializer baseNameAndValueEnumSerializer = new BaseNameAndValueEnumSerializer();
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
                .serializerByType(BaseHasNameEnum.class, baseNameAndValueEnumSerializer);
    }

    public static class LocalDateTimeI18nSerializer extends LocalDateTimeSerializer {

        private final LocalDateTimeSerializer delegate;

        public LocalDateTimeI18nSerializer(String dateFormat) {
            this(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateFormat)));
        }

        public LocalDateTimeI18nSerializer(LocalDateTimeSerializer delegate) {
            this.delegate = delegate;
        }

        protected LocalDateTimeI18nSerializer(LocalDateTimeSerializer delegate, Boolean useTimestamp, Boolean useNanoseconds, DateTimeFormatter f) {
            super(delegate, useTimestamp, useNanoseconds, f);
            this.delegate = delegate;
        }

        @Override
        protected LocalDateTimeI18nSerializer withFormat(Boolean useTimestamp, DateTimeFormatter f, JsonFormat.Shape shape) {
            return new LocalDateTimeI18nSerializer(delegate, useTimestamp, _useNanoseconds, f);
        }

        @Override
        public void serialize(LocalDateTime value, JsonGenerator g, SerializerProvider provider) throws IOException {
            delegate.serialize(convertTimeZone(value), g, provider);
        }

        @Override
        public void serializeWithType(LocalDateTime value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
            delegate.serializeWithType(convertTimeZone(value), g, provider, typeSer);
        }

        private LocalDateTime convertTimeZone(LocalDateTime value) {
            if (value == null) {
                return null;
            }
            return value.atZone(TimeZone.getDefault().toZoneId())
                    .withZoneSameInstant(LocaleContextHolder.getTimeZone().toZoneId())
                    .toLocalDateTime();
        }

    }

    public static class LocalDateTimeI18nDeserializer extends LocalDateTimeDeserializer {

        private final LocalDateTimeDeserializer delegate;

        public LocalDateTimeI18nDeserializer(String dateFormat) {
            this(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateFormat)));
        }

        public LocalDateTimeI18nDeserializer(LocalDateTimeDeserializer delegate) {
            this.delegate = delegate;
        }

        public LocalDateTimeI18nDeserializer(LocalDateTimeDeserializer delegate, DateTimeFormatter formatter) {
            super(formatter);
            this.delegate = delegate;
        }

        @Override
        protected LocalDateTimeI18nDeserializer withDateFormat(DateTimeFormatter formatter) {
            return new LocalDateTimeI18nDeserializer(delegate, formatter);
        }

        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            LocalDateTime localDateTime = delegate.deserialize(parser, context);
            if (localDateTime == null) {
                return null;
            }
            return localDateTime.atZone(TimeZone.getDefault().toZoneId())
                    .withZoneSameInstant(LocaleContextHolder.getTimeZone().toZoneId())
                    .toLocalDateTime();
        }

    }

    public static class BaseNameAndValueEnumSerializer extends JsonSerializer<BaseHasNameEnum<?>> {

        private final Map<Map.Entry<Class<?>, String>, Boolean> canWriteNameEnumCache = new ConcurrentHashMap<>();

        @Override
        public void serialize(BaseHasNameEnum<?> value,
                              JsonGenerator gen,
                              SerializerProvider serializers) throws IOException {
            gen.writeString(value.toString());
            writeEnumName(value, gen);
        }

        private void writeEnumName(BaseHasNameEnum<?> value, JsonGenerator gen) throws IOException {
            JsonStreamContext outputContext = gen.getOutputContext();
            Class<?> currentClass = outputContext.getCurrentValue().getClass();
            String currentName = outputContext.getCurrentName();
            String enumNameField = currentName + "Name";

            boolean canWriteNameEnum = canWriteNameEnumCache.computeIfAbsent(Map.entry(currentClass, currentName), k -> {

                Class<?> clazz = currentClass;
                while (clazz != null && clazz != Object.class) {
                    try {
                        clazz.getDeclaredField(enumNameField);
                        return false;
                    } catch (NoSuchFieldException e) {
                        clazz = clazz.getSuperclass();
                    }
                }
                return true;
            });

            if (canWriteNameEnum) {
                gen.writeStringField(enumNameField, value.getName());
            }
        }

    }

    private static class EnumBeanDeserializerFactory extends BeanDeserializerFactory {

        public static final EnumBeanDeserializerFactory INSTANCE = new EnumBeanDeserializerFactory(
                new DeserializerFactoryConfig());

        private final Map<Class<?>, JsonDeserializer<?>> jsonDeserializerCache;

        public EnumBeanDeserializerFactory(DeserializerFactoryConfig config) {
            super(config);
            this.jsonDeserializerCache = new ConcurrentHashMap<>();
        }

        @Override
        public DeserializerFactory withConfig(DeserializerFactoryConfig config) {
            if (_factoryConfig == config) {
                return this;
            }
            return new EnumBeanDeserializerFactory(config);
        }

        @Override
        protected JsonDeserializer<?> _findCustomEnumDeserializer(Class<?> type,
                                                                  DeserializationConfig config,
                                                                  BeanDescription beanDesc) {
            return jsonDeserializerCache.computeIfAbsent(type, EnumDeserializer::new);
        }
    }

    public static class EnumDeserializer extends JsonDeserializer<Enum<?>> {

        private final Class<Enum<?>> enumType;

        @SuppressWarnings("unchecked")
        public EnumDeserializer(Class<?> type) {
            if (type.isEnum()) {
                this.enumType = (Class<Enum<?>>) type;
            } else {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public Enum<?> deserialize(JsonParser p, DeserializationContext context) throws IOException {
            if (p.hasToken(JsonToken.VALUE_STRING)) {
                String text = p.getText();
                if (!StringUtils.hasText(text)) {
                    return null;
                }
                Enum<?>[] enumConstants = enumType.getEnumConstants();
                for (Enum<?> enumConstant : enumConstants) {
                    if (enumConstant.name().equalsIgnoreCase(text)) {
                        return enumConstant;
                    }
                }
                context.handleWeirdStringValue(enumType, text,
                        "not one of the values accepted for Enum class: %s", Arrays.toString(enumConstants));
            }
            if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                int index = p.getIntValue();
                Enum<?>[] enumConstants = enumType.getEnumConstants();
                for (Enum<?> enumConstant : enumConstants) {
                    if (enumConstant.ordinal() == index) {
                        return enumConstant;
                    }
                }
                context.handleWeirdNumberValue(enumType, index,
                        "index value outside legal index range [0..%s]",
                        enumConstants.length - 1);
            }
            return (Enum<?>) context.handleUnexpectedToken(enumType, p);
        }
    }

}

package com.tyrael.kharazim.web.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.NonNull;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
public class EnumConverterFactory implements ConverterFactory<String, Enum<?>>, ConditionalConverter {

    @Override
    @NonNull
    public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
        if (targetType.isEnum()) {
            T[] enumConstants = targetType.getEnumConstants();
            return source -> {
                if (StringUtils.isBlank(source)) {
                    return null;
                }
                for (T enumConstant : enumConstants) {
                    if (enumConstant.name().equalsIgnoreCase(source.trim())) {
                        return enumConstant;
                    }
                }
                throw new IllegalArgumentException(
                        "No enum constant " + targetType.getCanonicalName() + "." + source);
            };
        }
        throw new IllegalArgumentException("there is no enum converter for class: " + targetType.getCanonicalName());
    }

    @Override
    public boolean matches(@NonNull TypeDescriptor sourceType, @NonNull TypeDescriptor targetType) {
        return Enum.class.isAssignableFrom(targetType.getType())
                && sourceType.getType().equals(String.class);
    }
}

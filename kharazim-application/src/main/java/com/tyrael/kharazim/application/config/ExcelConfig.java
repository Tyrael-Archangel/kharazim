package com.tyrael.kharazim.application.config;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ConverterKeyBuild;
import com.alibaba.excel.converters.DefaultConverterLoader;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.tyrael.kharazim.KharazimApplication;
import com.tyrael.kharazim.common.converter.*;
import com.tyrael.kharazim.common.dto.BaseNameAndValueEnum;
import jakarta.annotation.PostConstruct;
import org.reflections.Reflections;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/5/22
 */
@Configuration
public class ExcelConfig {

    @PostConstruct
    public void init() {
        registerConverter(new LocalDateNumberConverter());
        registerConverter(new LocalDateStringConverter());
        registerConverter(new LocalDateTimeNumberConverter());
        registerConverter(new LocalDateTimeStringConverter());
        registerConverter(new LocalTimeNumberConverter());
        registerConverter(new LocalTimeStringConverter());
        registerConverter(new YesOrNoConverter());
        registerBaseNameAndValueEnumConverter();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void registerBaseNameAndValueEnumConverter() {
        Reflections reflections = new Reflections(KharazimApplication.class.getPackageName());
        Set<Class<? extends BaseNameAndValueEnum>> subTypes = reflections.getSubTypesOf(BaseNameAndValueEnum.class);
        for (Class<? extends BaseNameAndValueEnum> subType : subTypes) {
            if (subType.isEnum()) {
                registerConverter((BaseNameAndValueEnumConverter) () -> subType);
            }
        }
    }

    private <T> void registerConverter(Converter<T> converter) {
        Class<?> supportJavaTypeKey = converter.supportJavaTypeKey();
        CellDataTypeEnum supportedExcelTypeKey = converter.supportExcelTypeKey();
        DefaultConverterLoader.loadDefaultReadConverter()
                .put(ConverterKeyBuild.buildKey(supportJavaTypeKey, supportedExcelTypeKey), converter);
        DefaultConverterLoader.loadDefaultWriteConverter()
                .put(ConverterKeyBuild.buildKey(supportJavaTypeKey), converter);
    }

}

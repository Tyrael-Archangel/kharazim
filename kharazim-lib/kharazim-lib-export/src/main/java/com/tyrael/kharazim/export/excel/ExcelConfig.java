package com.tyrael.kharazim.export.excel;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.converters.ConverterKeyBuild;
import cn.idev.excel.converters.DefaultConverterLoader;
import cn.idev.excel.enums.CellDataTypeEnum;
import com.tyrael.kharazim.base.dto.BaseHasNameEnum;
import com.tyrael.kharazim.export.excel.converter.*;
import org.reflections.Reflections;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/5/22
 */
@Configuration
public class ExcelConfig implements InitializingBean {

    @Value("${excel.enum.scans:com.tyrael.kharazim}")
    private String enumScanPaths;

    @Override
    public void afterPropertiesSet() {
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
        if (ObjectUtils.isEmpty(enumScanPaths)) {
            return;
        }
        String[] enumScanPathArray = enumScanPaths.split(",");
        Set<Class<? extends BaseHasNameEnum>> subTypes = Arrays.stream(enumScanPathArray)
                .filter(path -> !ObjectUtils.isEmpty(path))
                .map(path -> new Reflections(path).getSubTypesOf(BaseHasNameEnum.class))
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        for (Class<? extends BaseHasNameEnum> subType : subTypes) {
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

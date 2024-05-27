package com.tyrael.kharazim.common.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.DateTimeFormatProperty;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.excel.util.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Optional;

/**
 * @author Tyrael Archangel
 * @since 2024/5/22
 */
public abstract class DateTimeConverter<T extends Temporal> implements Converter<T> {

    /**
     * 默认时间/日期格式
     *
     * @return 格式
     */
    protected abstract String getDefaultDateTimeFormat();

    @Override
    public WriteCellData<?> convertToExcelData(T value,
                                               ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        if (value == null) {
            return new WriteCellData<>();
        }

        String dateFormat = Optional.ofNullable(contentProperty)
                .map(ExcelContentProperty::getDateTimeFormatProperty)
                .map(DateTimeFormatProperty::getFormat)
                .filter(StringUtils::isNotBlank)
                .orElseGet(this::getDefaultDateTimeFormat);

        DateTimeFormatter dateTimeFormatter = Optional.ofNullable(globalConfiguration.getLocale())
                .map(locale -> DateTimeFormatter.ofPattern(dateFormat, locale))
                .orElseGet(() -> DateTimeFormatter.ofPattern(dateFormat));

        return new WriteCellData<>(dateTimeFormatter.format(value));
    }

    /**
     * getLocalDateTime
     *
     * @param numberValue         numberValue
     * @param contentProperty     {@link ExcelContentProperty}
     * @param globalConfiguration {@link GlobalConfiguration}
     * @return LocalDateTime
     */
    protected LocalDateTime getLocalDateTime(BigDecimal numberValue,
                                             ExcelContentProperty contentProperty,
                                             GlobalConfiguration globalConfiguration) {
        Boolean use1904windowing = Optional.ofNullable(contentProperty)
                .map(ExcelContentProperty::getDateTimeFormatProperty)
                .map(DateTimeFormatProperty::getUse1904windowing)
                .orElseGet(globalConfiguration::getUse1904windowing);

        return DateUtil.getLocalDateTime(
                numberValue.doubleValue(),
                Boolean.TRUE.equals(use1904windowing));
    }

    /**
     * getLocalDateTime
     *
     * @param dateString          dateString
     * @param contentProperty     {@link ExcelContentProperty}
     * @param globalConfiguration {@link GlobalConfiguration}
     * @return LocalDateTime
     */
    protected LocalDateTime getLocalDateTime(String dateString,
                                             ExcelContentProperty contentProperty,
                                             GlobalConfiguration globalConfiguration) {

        String dateFormat = Optional.ofNullable(contentProperty)
                .map(ExcelContentProperty::getDateTimeFormatProperty)
                .map(DateTimeFormatProperty::getFormat)
                .filter(StringUtils::isNotBlank)
                .orElseGet(() -> DateUtils.switchDateFormat(dateString));

        DateTimeFormatter dateTimeFormatter = Optional.ofNullable(globalConfiguration.getLocale())
                .map(locale -> DateTimeFormatter.ofPattern(dateFormat, locale))
                .orElseGet(() -> DateTimeFormatter.ofPattern(dateFormat));

        return LocalDateTime.parse(dateString, dateTimeFormatter);
    }

}

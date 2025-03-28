package com.tyrael.kharazim.export.excel.converter;

import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/5/22
 */
public class LocalDateTimeNumberConverter extends DateTimeConverter<LocalDateTime> {

    @Override
    public Class<LocalDateTime> supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    @Override
    public LocalDateTime convertToJavaData(ReadCellData<?> cellData,
                                           ExcelContentProperty contentProperty,
                                           GlobalConfiguration globalConfiguration) {
        BigDecimal numberValue = cellData.getNumberValue();
        if (numberValue == null) {
            return null;
        }
        return super.getLocalDateTime(numberValue, contentProperty, globalConfiguration);
    }

    @Override
    protected String getDefaultDateTimeFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }
}

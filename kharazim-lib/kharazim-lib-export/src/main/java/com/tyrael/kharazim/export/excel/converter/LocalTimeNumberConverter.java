package com.tyrael.kharazim.export.excel.converter;

import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * @author Tyrael Archangel
 * @since 2024/5/22
 */
public class LocalTimeNumberConverter extends DateTimeConverter<LocalTime> {

    @Override
    public Class<LocalTime> supportJavaTypeKey() {
        return LocalTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    @Override
    public LocalTime convertToJavaData(ReadCellData<?> cellData,
                                       ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) {
        BigDecimal numberValue = cellData.getNumberValue();
        if (numberValue == null) {
            return null;
        }
        return super.getLocalDateTime(numberValue, contentProperty, globalConfiguration).toLocalTime();
    }

    @Override
    protected String getDefaultDateTimeFormat() {
        return "HH:mm:ss";
    }
}

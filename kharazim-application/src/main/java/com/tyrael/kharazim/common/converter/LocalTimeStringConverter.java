package com.tyrael.kharazim.common.converter;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.StringUtils;

import java.time.LocalTime;

/**
 * @author Tyrael Archangel
 * @since 2024/5/22
 */
public class LocalTimeStringConverter extends DateTimeConverter<LocalTime> {

    @Override
    public Class<LocalTime> supportJavaTypeKey() {
        return LocalTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalTime convertToJavaData(ReadCellData<?> cellData,
                                       ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) {
        String dateString = cellData.getStringValue();
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        return super.getLocalDateTime(dateString, contentProperty, globalConfiguration).toLocalTime();
    }

    @Override
    protected String getDefaultDateTimeFormat() {
        return "HH:mm:ss";
    }
}

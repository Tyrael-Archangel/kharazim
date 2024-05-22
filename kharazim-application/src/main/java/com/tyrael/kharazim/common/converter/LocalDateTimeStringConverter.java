package com.tyrael.kharazim.common.converter;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.StringUtils;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/5/22
 */
public class LocalDateTimeStringConverter extends DateTimeConverter<LocalDateTime> {

    @Override
    public Class<LocalDateTime> supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(ReadCellData<?> cellData,
                                           ExcelContentProperty contentProperty,
                                           GlobalConfiguration globalConfiguration) {
        String dateString = cellData.getStringValue();
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        return super.getLocalDateTime(dateString, contentProperty, globalConfiguration);
    }

    @Override
    protected String getDefaultDateTimeFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }
}

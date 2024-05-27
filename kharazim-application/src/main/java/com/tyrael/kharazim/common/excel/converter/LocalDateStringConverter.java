package com.tyrael.kharazim.common.excel.converter;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.StringUtils;

import java.time.LocalDate;

/**
 * @author Tyrael Archangel
 * @since 2024/5/22
 */
public class LocalDateStringConverter extends DateTimeConverter<LocalDate> {

    @Override
    public Class<LocalDate> supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDate convertToJavaData(ReadCellData<?> cellData,
                                       ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) {
        String dateString = cellData.getStringValue();
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        return super.getLocalDateTime(dateString, contentProperty, globalConfiguration).toLocalDate();
    }

    @Override
    protected String getDefaultDateTimeFormat() {
        return "yyyy-MM-dd";
    }
}

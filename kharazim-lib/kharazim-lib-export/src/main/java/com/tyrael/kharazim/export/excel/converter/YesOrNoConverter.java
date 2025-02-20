package com.tyrael.kharazim.export.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Tyrael Archangel
 * @since 2024/5/22
 */
public class YesOrNoConverter implements Converter<Boolean> {

    @Override
    public Class<Boolean> supportJavaTypeKey() {
        return Boolean.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Boolean convertToJavaData(ReadCellData<?> cellData,
                                     ExcelContentProperty contentProperty,
                                     GlobalConfiguration globalConfiguration) {
        String stringValue = cellData.getStringValue();
        if (StringUtils.isBlank(stringValue)) {
            return null;
        }
        if (StringUtils.equalsAnyIgnoreCase(stringValue.trim(), "是", "true", "yes")) {
            return Boolean.TRUE;
        }
        if (StringUtils.equalsAnyIgnoreCase(stringValue.trim(), "否", "false", "no")) {
            return Boolean.FALSE;
        }
        return null;
    }

    @Override
    public WriteCellData<String> convertToExcelData(Boolean value,
                                                    ExcelContentProperty contentProperty,
                                                    GlobalConfiguration globalConfiguration) {
        if (value == null) {
            return new WriteCellData<>();
        }
        return new WriteCellData<>(value ? "是" : "否");
    }
}

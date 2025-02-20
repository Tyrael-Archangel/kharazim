package com.tyrael.kharazim.export.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.tyrael.kharazim.base.dto.BaseHasNameEnum;

import java.util.Arrays;

/**
 * @author Tyrael Archangel
 * @since 2024/5/22
 */
public interface BaseNameAndValueEnumConverter<T extends Enum<T> & BaseHasNameEnum<T>> extends Converter<T> {

    @Override
    Class<T> supportJavaTypeKey();

    @Override
    default CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    default T convertToJavaData(ReadCellData<?> cellData,
                                ExcelContentProperty contentProperty,
                                GlobalConfiguration globalConfiguration) {
        String stringValue = cellData.getStringValue();
        if (stringValue == null || stringValue.isBlank()) {
            return null;
        }

        stringValue = stringValue.trim();
        T[] enumConstants = supportJavaTypeKey().getEnumConstants();
        for (T enumConstant : enumConstants) {
            if (stringValue.equalsIgnoreCase(enumConstant.getName()) || stringValue.equalsIgnoreCase(enumConstant.name())) {
                return enumConstant;
            }
        }

        throw new IllegalArgumentException("Unknown value '" + stringValue + "' accepted for " + Arrays.toString(enumConstants));
    }

    @Override
    default WriteCellData<String> convertToExcelData(T value,
                                                     ExcelContentProperty contentProperty,
                                                     GlobalConfiguration globalConfiguration) {
        if (value == null) {
            return new WriteCellData<>();
        }
        return new WriteCellData<>(value.getName());
    }

}

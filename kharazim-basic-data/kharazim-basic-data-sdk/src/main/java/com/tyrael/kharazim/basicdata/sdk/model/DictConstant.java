package com.tyrael.kharazim.basicdata.sdk.model;

import com.tyrael.kharazim.base.dto.BaseHasNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
@Getter
@AllArgsConstructor
public sealed class DictConstant implements Serializable permits DictConstant.EnumDict {

    private final String code;
    private final String desc;

    public static DictConstant dict(String code, String desc) {
        return new DictConstant(code, desc);
    }

    public static <T extends Enum<T> & BaseHasNameEnum<T>> DictConstant.EnumDict<T> dict(
            String code, Class<T> enumClass, String desc) {
        return new DictConstant.EnumDict<>(code, enumClass, desc);
    }

    @ToString
    public static final class EnumDict<T extends Enum<T> & BaseHasNameEnum<T>> extends DictConstant {

        private final Class<T> relatedEnum;
        private final Map<String, String> items;

        EnumDict(String code, Class<T> relatedEnum, String desc) {
            super(code, desc);
            this.relatedEnum = relatedEnum;
            this.items = Arrays.stream(this.relatedEnum.getEnumConstants())
                    .collect(Collectors.toMap(Enum::name, BaseHasNameEnum::getName, (e1, e2) -> e1, LinkedHashMap::new));
        }

        public Map<String, String> getDictItems() {
            return new LinkedHashMap<>(items);
        }
    }

}

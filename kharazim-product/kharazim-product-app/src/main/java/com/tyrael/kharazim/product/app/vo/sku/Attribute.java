package com.tyrael.kharazim.product.app.vo.sku;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/3/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {

    @Schema(description = "属性名")
    private String name;

    @Schema(description = "属性值")
    private String value;

    public static String join(Collection<Attribute> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return "";
        }
        return attributes.stream()
                .map(Attribute::join)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(", "));
    }

    public static String join(Attribute attribute) {
        if (attribute == null) {
            return "";
        }
        String name = Objects.toString(attribute.getName(), "");
        String value = Objects.toString(attribute.getValue(), "");
        if (StringUtils.isBlank(name)) {
            return value;
        } else {
            return name + ": " + value;
        }
    }

}

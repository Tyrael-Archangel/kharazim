package com.tyrael.kharazim.application.system.dto.requestlog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NameAndValue {

    private String name;
    private String value;

    @Override
    public String toString() {
        return name + "=" + value;
    }
}
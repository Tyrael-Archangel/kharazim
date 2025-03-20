package com.tyrael.kharazim.basicdata.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Tyrael Archangel
 * @since 2025/3/20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicVO implements Serializable {

    private String code;

    private String name;

    private String englishName;

    private String image;

    private String imageUrl;

}

package com.tyrael.kharazim.basicdata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitDictRequest implements Serializable {

    private DictVO dict;

    private List<DictItemVO> dictItems;

}

package com.tyrael.kharazim.basicdata.sdk.service;

import com.tyrael.kharazim.basicdata.model.DictConstant;
import com.tyrael.kharazim.basicdata.model.DictItemVO;
import com.tyrael.kharazim.basicdata.model.DictVO;
import com.tyrael.kharazim.basicdata.model.InitDictRequest;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
public interface DictServiceApi {

    /**
     * list dict items by code
     *
     * @param dictCode dictCode
     * @return dict items
     */
    List<DictItemVO> listItems(String dictCode);

    /**
     * list dict items by DictConstant
     *
     * @param dict DictConstant
     * @return dict items
     */
    default List<DictItemVO> listItems(DictConstant dict) {
        return listItems(dict.getCode());
    }

    /**
     * init system dict
     *
     * @param initDictRequest {@link InitDictRequest}
     */
    void initSystemDict(InitDictRequest initDictRequest);

    /**
     * init DictConstants
     *
     * @param dict DictConstant
     */
    default void init(DictConstant dict) {

        DictVO dictVO;
        List<DictItemVO> dictItems;

        if (dict instanceof DictConstant.EnumDict<?> enumDict) {
            dictVO = new DictVO(dict.getCode(), dict.getDesc(), false);
            AtomicInteger sort = new AtomicInteger(1);
            dictItems = enumDict.getDictItems().entrySet()
                    .stream()
                    .map(entry -> new DictItemVO(
                            dictVO.getCode(),
                            entry.getKey(),
                            entry.getValue(),
                            sort.getAndIncrement()))
                    .toList();
        } else {
            dictVO = new DictVO(dict.getCode(), dict.getDesc(), true);
            dictItems = Collections.emptyList();
        }

        initSystemDict(new InitDictRequest(dictVO, dictItems));
    }

}

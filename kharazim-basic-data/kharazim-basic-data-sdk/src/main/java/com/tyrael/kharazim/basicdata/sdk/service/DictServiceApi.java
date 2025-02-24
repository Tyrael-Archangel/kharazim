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
     * init system dict
     *
     * @param initDictRequest {@link InitDictRequest}
     */
    void initSystemDict(InitDictRequest initDictRequest);

    /**
     * dubbo中会default方法也会被代理，因此default不能直接定义在接口中
     */
    final class Defaults {

        private final DictServiceApi dictServiceApi;

        public Defaults(DictServiceApi dictServiceApi) {
            this.dictServiceApi = dictServiceApi;
        }

        /**
         * list dict items by DictConstant
         *
         * @param dict DictConstant
         * @return dict items
         */
        public List<DictItemVO> listItems(DictConstant dict) {
            return dictServiceApi.listItems(dict.getCode());
        }

        /**
         * init DictConstants
         *
         * @param dict DictConstant
         */
        public void init(DictConstant dict) {

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

            dictServiceApi.initSystemDict(new InitDictRequest(dictVO, dictItems));
        }
    }

}

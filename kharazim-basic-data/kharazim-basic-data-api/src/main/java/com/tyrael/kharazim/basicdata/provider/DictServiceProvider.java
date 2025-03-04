package com.tyrael.kharazim.basicdata.provider;

import com.tyrael.kharazim.basicdata.app.service.dict.DictService;
import com.tyrael.kharazim.basicdata.sdk.model.DictItemVO;
import com.tyrael.kharazim.basicdata.sdk.model.InitDictRequest;
import com.tyrael.kharazim.basicdata.sdk.service.DictServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
@Component
@DubboService
@RequiredArgsConstructor
public class DictServiceProvider implements DictServiceApi {

    private final DictService dictService;

    @Override
    public List<DictItemVO> listItems(String dictCode) {
        return dictService.listItems(dictCode)
                .stream()
                .map(e -> DictItemVO.builder()
                        .dictCode(e.getDictCode())
                        .dictDesc(e.getDictDesc())
                        .key(e.getKey())
                        .value(e.getValue())
                        .sort(e.getSort())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void initSystemDict(InitDictRequest initDictRequest) {
        dictService.initSystemDict(initDictRequest);
    }

}

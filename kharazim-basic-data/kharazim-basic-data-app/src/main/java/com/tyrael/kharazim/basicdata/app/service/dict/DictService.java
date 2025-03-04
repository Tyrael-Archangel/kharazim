package com.tyrael.kharazim.basicdata.app.service.dict;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.basicdata.app.dto.dict.DictDTO;
import com.tyrael.kharazim.basicdata.app.dto.dict.DictItemDTO;
import com.tyrael.kharazim.basicdata.app.dto.dict.PageDictRequest;
import com.tyrael.kharazim.basicdata.app.dto.dict.SaveDictItemRequest;
import com.tyrael.kharazim.basicdata.sdk.model.InitDictRequest;
import com.tyrael.kharazim.user.sdk.model.AuthUser;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
public interface DictService {

    /**
     * 字典分页查询
     *
     * @param pageDictRequest PageDictRequest
     * @return 字典分页数据
     */
    PageResponse<DictDTO> pageDict(PageDictRequest pageDictRequest);

    /**
     * 字典项列表
     *
     * @param dictCode 字典编码
     * @return 字典项列表数据
     */
    List<DictItemDTO> listItems(String dictCode);

    /**
     * 初始化系统字典
     *
     * @param initDictRequest InitDictRequest
     */
    void initSystemDict(InitDictRequest initDictRequest);

    /**
     * 添加字典项
     *
     * @param addDictItemRequest SaveDictItemRequest
     * @return 新增的字典项ID
     */
    Long addDictItem(SaveDictItemRequest addDictItemRequest);

    /**
     * 修改字典项
     *
     * @param dictItemId            字典项ID
     * @param modifyDictItemRequest SaveDictItemRequest
     * @param currentUser           操作人
     */
    void modifyDictItem(Long dictItemId, SaveDictItemRequest modifyDictItemRequest, AuthUser currentUser);

}

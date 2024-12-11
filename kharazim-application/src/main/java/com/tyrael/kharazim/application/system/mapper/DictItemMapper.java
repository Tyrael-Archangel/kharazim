package com.tyrael.kharazim.application.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.base.BasePageMapper;
import com.tyrael.kharazim.application.system.domain.DictItem;
import com.tyrael.kharazim.application.system.dto.dict.PageDictItemRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collections;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Mapper
public interface DictItemMapper extends BasePageMapper<DictItem> {

    default DictItem finByDictCodeAndItemValue(String dictCode, String itemValue) {
        LambdaQueryWrapper<DictItem> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DictItem::getDictCode, dictCode)
                .eq(DictItem::getValue, itemValue);
        return selectOne(queryWrapper);
    }

    /**
     * list by dictCode
     *
     * @param dictCode 字典编码
     * @return entities
     */
    default List<DictItem> listByDictCode(String dictCode) {
        if (StringUtils.isBlank(dictCode)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<DictItem> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DictItem::getDictCode, dictCode);
        return this.selectList(queryWrapper);
    }

    /**
     * page
     *
     * @param pageRequest PageDictItemRequest
     * @return entities
     */
    default PageResponse<DictItem> page(PageDictItemRequest pageRequest) {
        LambdaQueryWrapper<DictItem> queryWrapper = Wrappers.lambdaQuery();
        String dictCode = pageRequest.getTypeCode();
        if (StringUtils.isNotBlank(dictCode)) {
            queryWrapper.eq(DictItem::getDictCode, dictCode);
        }

        String keywords = pageRequest.getKeywords();
        if (StringUtils.isNotBlank(keywords)) {
            queryWrapper.and(q -> q.like(DictItem::getKey, keywords)
                    .or()
                    .like(DictItem::getValue, keywords));
        }

        queryWrapper.orderByAsc(DictItem::getSort)
                .orderByAsc(DictItem::getId);

        return page(pageRequest, queryWrapper);
    }

}

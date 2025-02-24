package com.tyrael.kharazim.basicdata.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.basicdata.app.domain.DictItem;
import com.tyrael.kharazim.mybatis.BasePageMapper;
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

    /**
     * find by dictCode and item key
     *
     * @param dictCode 字典编码
     * @param itemKey  key
     * @return DictItem
     */
    default DictItem finByDictCodeAndItemKey(String dictCode, String itemKey) {
        LambdaQueryWrapper<DictItem> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DictItem::getDictCode, dictCode)
                .eq(DictItem::getKey, itemKey);
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

}

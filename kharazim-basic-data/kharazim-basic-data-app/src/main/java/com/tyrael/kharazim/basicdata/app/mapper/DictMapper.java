package com.tyrael.kharazim.basicdata.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tyrael.kharazim.basicdata.app.domain.Dict;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Mapper
public interface DictMapper extends BasePageMapper<Dict> {

    /**
     * all dict
     *
     * @return dictionaries
     */
    default List<Dict> all() {
        return selectList(new LambdaQueryWrapper<>());
    }

    /**
     * find by code
     *
     * @param dictCode dictCode
     * @return dict
     */
    default Dict findByCode(String dictCode) {
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dict::getCode, dictCode);
        return selectOne(queryWrapper);
    }
}

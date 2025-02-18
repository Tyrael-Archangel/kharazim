package com.tyrael.kharazim.idgenerator.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.idgenerator.app.entity.CodeAllocate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@Mapper
public interface CodeAllocateMapper extends BaseMapper<CodeAllocate> {

    /**
     * get by tag
     *
     * @param tag tag
     * @return CodeAllocate
     */
    default CodeAllocate getByTag(String tag) {
        LambdaQueryWrapper<CodeAllocate> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CodeAllocate::getTag, tag);
        return selectOne(queryWrapper);
    }

    /**
     * increase step
     *
     * @param tag  tag
     * @param step step
     * @return CodeAllocate
     */
    default CodeAllocate increaseStep(String tag, int step) {
        int updated = increaseNextValueByStep(tag, step);
        return updated > 0 ? getByTag(tag) : null;
    }

    /**
     * increase max value by step
     *
     * @param tag  tag
     * @param step step
     * @return updated rows
     */
    @Update("update `code_allocate` set `next_value` = `next_value` + #{step} where `tag` = #{tag}")
    int increaseNextValueByStep(@Param("tag") String tag, @Param("step") int step);

}

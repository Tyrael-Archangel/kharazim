package com.tyrael.kharazim.lib.idgenerator.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.lib.idgenerator.entity.CodeAllocate;
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
     * create table
     */
    @Update("""
            create table if not exists `code_allocate`
            (
                `id`           bigint      not null primary key auto_increment,
                `tag`          varchar(64) not null,
                `next_value`   bigint      not null,
                `created_time` datetime    not null,
                unique index udx_tag (`tag`)
            );
            """)
    void createTable();

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

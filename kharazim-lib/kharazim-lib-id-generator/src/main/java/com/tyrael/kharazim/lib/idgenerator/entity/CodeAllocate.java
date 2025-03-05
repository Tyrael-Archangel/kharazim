package com.tyrael.kharazim.lib.idgenerator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <pre>
 *  create table `code_allocate`
 *  (
 *     `id`           bigint      not null primary key auto_increment,
 *     `tag`          varchar(64) not null,
 *     `next_value`   bigint      not null,
 *     `created_time` datetime    not null,
 *     unique index udx_tag (`tag`)
 *  );
 * </pre>
 *
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@Data
public class CodeAllocate {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String tag;

    private Long nextValue;

    private LocalDateTime createdTime;

    public static CodeAllocate createNew(String tag, int step) {
        CodeAllocate codeAllocate = new CodeAllocate();
        codeAllocate.tag = tag;
        codeAllocate.nextValue = step + 1L;
        codeAllocate.createdTime = LocalDateTime.now();
        return codeAllocate;
    }

}

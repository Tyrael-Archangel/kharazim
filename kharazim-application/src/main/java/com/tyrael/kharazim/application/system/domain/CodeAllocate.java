package com.tyrael.kharazim.application.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
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

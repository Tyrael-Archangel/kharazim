package com.tyrael.kharazim.application.system.service.impl;

import com.tyrael.kharazim.application.config.TagStepConfig;
import com.tyrael.kharazim.application.system.domain.CodeAllocate;
import com.tyrael.kharazim.application.system.mapper.CodeAllocateMapper;
import com.tyrael.kharazim.common.exception.ShouldNotHappenException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "system.global.code-generator", havingValue = "datasource")
public class DataSourceCodeGenerator extends AbstractCodeGenerator {

    private final CodeAllocateMapper codeAllocateMapper;
    private final PlatformTransactionManager transactionManager;
    private final TagStepConfig tagStepConfig;
    private final Map<String, TagCache> tagCacheMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("Use DataSource as CodeGenerator");
    }

    @Override
    public long increment(String tag) {

        // 不断重试，类似CAS，添加最大重试次数，避免意外的死循环
        int maxCasTime = 100;
        while (maxCasTime-- > 0) {
            TagCache tagCache = loadCache(tag);
            long nextValue = tagCache.nextValue();
            if (nextValue > 0L) {
                // 如果nextValue有效，直接返回，否则删除缓存，然后重新获取
                return nextValue;
            } else {
                evictCache(tag);
            }
        }
        throw new ShouldNotHappenException("increase error, tag: " + tag);
    }

    private TagCache loadCache(String tag) {
        synchronized (tag.intern()) {
            TagCache cache = tagCacheMap.get(tag);
            if (cache == null) {
                TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
                transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
                cache = transactionTemplate.execute(status -> loadCacheAndIncreaseStep(tag));
                tagCacheMap.put(tag, cache);
            }
            if (cache == null) {
                throw new ShouldNotHappenException("load cache error.");
            }
            return cache;
        }
    }

    private TagCache loadCacheAndIncreaseStep(String tag) {

        int step = tagStepConfig.getStep(tag);
        CodeAllocate codeAllocate = codeAllocateMapper.increaseStep(tag, step);

        if (codeAllocate == null) {
            codeAllocate = CodeAllocate.createNew(tag, step);
            try {
                codeAllocateMapper.insert(codeAllocate);
            } catch (DuplicateKeyException e) {
                codeAllocate = codeAllocateMapper.increaseStep(tag, step);
            }
        }

        return new TagCache(tag, codeAllocate.getNextValue() - step, step);
    }

    private void evictCache(String tag) {
        tagCacheMap.remove(tag);
    }

    @Getter
    private static class TagCache {

        private final String tag;
        private final int step;
        private final long limitValue;
        private final AtomicLong value;

        private TagCache(String tag, long initValue, int step) {
            this.tag = tag;
            this.limitValue = initValue + step;
            this.step = step;
            this.value = new AtomicLong(initValue);
        }

        public long nextValue() {
            long nextValue = value.getAndIncrement();
            if (nextValue >= limitValue) {
                return -1L;
            }
            return nextValue;
        }
    }

}

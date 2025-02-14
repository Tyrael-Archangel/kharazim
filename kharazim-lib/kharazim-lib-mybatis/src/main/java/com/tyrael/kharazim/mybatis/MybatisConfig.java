package com.tyrael.kharazim.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.tyrael.kharazim.user.api.sdk.handler.AuthUserHolder;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * FIXME 不应该在通用的mybatis配置中引入对user-api-sdk的依赖
 *
 * @author Tyrael Archangel
 * @since 2024/1/5
 */
@Configuration
public class MybatisConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public DefaultDBFieldHandler defaultDBFieldHandler() {
        return new DefaultDBFieldHandler();
    }

    public static class DefaultDBFieldHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO baseDO) {

                LocalDateTime current = LocalDateTime.now();
                // 创建时间为空，则以当前时间为插入时间
                if (Objects.isNull(baseDO.getCreateTime())) {
                    baseDO.setCreateTime(current);
                }
                // 更新时间为空，则以当前时间为更新时间
                if (Objects.isNull(baseDO.getUpdateTime())) {
                    baseDO.setUpdateTime(current);
                }

                AuthUser currentUser = AuthUserHolder.getCurrentUser();
                if (currentUser != null) {
                    if (baseDO.getCreator() == null && baseDO.getCreatorCode() == null) {
                        baseDO.setCreator(currentUser.getNickName());
                        baseDO.setCreatorCode(currentUser.getCode());
                    }
                    if (baseDO.getUpdater() == null && baseDO.getUpdaterCode() == null) {
                        baseDO.setUpdater(currentUser.getNickName());
                        baseDO.setUpdaterCode(currentUser.getCode());
                    }
                }
            }
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO baseDO) {
                if (baseDO.getUpdateTime() == null) {
                    baseDO.setUpdateTime(LocalDateTime.now());
                }
                AuthUser currentUser = AuthUserHolder.getCurrentUser();
                if (currentUser != null) {
                    if (baseDO.getUpdater() == null && baseDO.getUpdaterCode() == null) {
                        baseDO.setUpdater(currentUser.getNickName());
                        baseDO.setUpdaterCode(currentUser.getCode());
                    }
                }
            }
        }
    }

}

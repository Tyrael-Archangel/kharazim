package com.tyrael.kharazim.lib.idgenerator.config;

import com.tyrael.kharazim.lib.idgenerator.service.DataSourceIdGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * @author Tyrael Archangel
 * @since 2025/3/5
 */
@MapperScan("com.tyrael.kharazim.lib.idgenerator.mapper")
@EnableConfigurationProperties(TagStepConfig.class)
@Import(DataSourceIdGenerator.class)
public class IdGeneratorAutoConfiguration {
}

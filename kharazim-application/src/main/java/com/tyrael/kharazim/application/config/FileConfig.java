package com.tyrael.kharazim.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author Tyrael Archangel
 * @since 2024/4/13
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileConfig {

    /**
     * 文件保存根路径
     */
    private String homePath;

    /**
     * root路径名
     */
    private String rootDir = ".kharazim_files";

    /**
     * 最大目录深度
     */
    private int maxDirLevel = 3;

    /**
     * 存储文件的目录下最多可以存放的文件数
     */
    private int maxFileCountAtLeafDir = 20;

    /**
     * 每级目录下最多可以创建的子目录数量
     */
    private int maxDirCountEveryLevel = 5;

    /**
     * 文件服务器schema
     */
    private String schema = "http://localhost:9408/kharazim-api";

    /**
     * 文件在客户端缓存时间
     */
    private Duration maxAge = Duration.ofMinutes(5);

    public String getHomePathOrDefault() {
        if (homePath == null || homePath.trim().isEmpty()) {
            return System.getProperty("user.home");
        }
        return homePath;
    }

}

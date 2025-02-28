package com.tyrael.kharazim.gateway.filter;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.PathContainer;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/2/28
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "system.request-log")
public class SystemRequestLogPathMather {

    private PathPatternMatcher pathPatternMatcher;
    private List<String> ignoreUrls;

    @PostConstruct
    public void init() {
        this.pathPatternMatcher = new PathPatternMatcher(this.ignoreUrls);
    }

    public boolean ignoreRequestLog(PathContainer pathContainer) {
        return pathPatternMatcher != null && pathPatternMatcher.match(pathContainer);
    }

}

package com.tyrael.kharazim.gateway.filter;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.PathContainer;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/2/13
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "auth.login")
public class AuthWhiteListChecker {

    private PathPatternMatcher pathPatternMatcher;

    private List<String> whitelist;

    @PostConstruct
    public void init() {
        this.pathPatternMatcher = new PathPatternMatcher(this.whitelist);
    }

    public boolean isWhite(PathContainer pathContainer) {
        return pathPatternMatcher != null && pathPatternMatcher.match(pathContainer);
    }

}

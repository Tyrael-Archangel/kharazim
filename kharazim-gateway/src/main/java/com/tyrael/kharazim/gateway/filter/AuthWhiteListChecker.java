package com.tyrael.kharazim.gateway.filter;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/2/13
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "auth.login")
public class AuthWhiteListChecker {

    private WhiteListPatternChecker whiteListUriChecker;

    private List<String> whitelist;

    @PostConstruct
    public void init() {
        this.whiteListUriChecker = new WhiteListPatternChecker(this.whitelist);
    }

    public boolean isWhite(PathContainer pathContainer) {
        return whiteListUriChecker != null && whiteListUriChecker.isWhite(pathContainer);
    }

    private static class WhiteListPatternChecker {

        private final List<Pattern> patterns;
        private final List<PathPattern> pathPatterns;

        public WhiteListPatternChecker(List<String> whitelist) {
            PathPatternParser pathPatternParser = new PathPatternParser();
            this.pathPatterns = Optional.ofNullable(whitelist)
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .map(pathPatternParser::parse)
                    .collect(Collectors.toList());
            this.patterns = Optional.ofNullable(whitelist)
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .map(Pattern::compile)
                    .collect(Collectors.toList());
        }

        public boolean isWhite(PathContainer pathContainer) {
            for (PathPattern pathPattern : pathPatterns) {
                if (pathPattern.matches(pathContainer)) {
                    return true;
                }
            }
            for (Pattern pattern : patterns) {
                if (pattern.matcher(pathContainer.value()).find()) {
                    return true;
                }
            }
            return false;
        }
    }

}

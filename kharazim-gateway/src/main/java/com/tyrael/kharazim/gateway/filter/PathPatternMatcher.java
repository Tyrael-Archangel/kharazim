package com.tyrael.kharazim.gateway.filter;

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
 * @since 2025/2/28
 */
class PathPatternMatcher {

    private final List<Pattern> patterns;
    private final List<PathPattern> pathPatterns;

    public PathPatternMatcher(List<String> whitelist) {
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

    boolean match(PathContainer pathContainer) {
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

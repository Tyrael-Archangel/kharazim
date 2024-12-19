package com.tyrael.kharazim.application.base.auth;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tyrael.kharazim.common.util.CollectionUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.RequestPath;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.ServletRequestPathUtils;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import org.springframework.web.util.pattern.PatternParseException;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/24
 */
public class RequestPathMatcher {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private final List<PathMatcher> patterns;
    private final Cache<String, Boolean> pathMatchCache = Caffeine.newBuilder()
            .maximumSize(2000)
            .expireAfterAccess(Duration.ofMinutes(20))
            .build();

    public RequestPathMatcher(List<String> patterns) {
        if (CollectionUtils.isEmpty(patterns)) {
            this.patterns = Collections.emptyList();
        } else {
            this.patterns = patterns.stream()
                    .map(PathMatcher::new)
                    .toList();
        }
    }

    public boolean matches(HttpServletRequest request) {
        if (CollectionUtils.isEmpty(patterns)) {
            return false;
        }
        return pathMatchCache.get(request.getRequestURI(), k -> anyMatch(request));
    }

    private boolean anyMatch(HttpServletRequest request) {
        System.out.println("===== pathMatchCache.estimatedSize: " + pathMatchCache.estimatedSize());
        return patterns.stream()
                .anyMatch(pattern -> pattern.match(request));
    }

    private static class PathMatcher {

        private final String patternString;
        private final PathPattern pathPattern;

        public PathMatcher(String pattern) {
            this.patternString = pattern;
            PathPattern tempPathPattern;
            try {
                tempPathPattern = PathPatternParser.defaultInstance.parse(pattern);
            } catch (PatternParseException e) {
                tempPathPattern = null;
            }
            this.pathPattern = tempPathPattern;
        }

        public boolean match(HttpServletRequest request) {

            Object path;
            try {
                path = ServletRequestPathUtils.getCachedPath(request);
            } catch (Exception e) {
                path = RequestPath.parse(request.getRequestURI(), request.getContextPath());
            }

            if (path instanceof PathContainer pathContainer) {
                if (this.pathPattern != null) {
                    return this.pathPattern.matches(pathContainer);
                }
                String lookupPath = pathContainer.value();
                path = UrlPathHelper.defaultInstance.removeSemicolonContent(lookupPath);
            }
            return PATH_MATCHER.match(this.patternString, (String) path);
        }

    }

}

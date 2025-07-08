package com.tyrael.kharazim.gateway.filter;

import com.tyrael.kharazim.user.sdk.vo.ClientInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Tyrael Archangel
 * @since 2025/7/7
 */
@Component
@RequiredArgsConstructor
public class RateLimiterFilter implements GlobalFilter, Ordered {

    private final RequestClientInfoResolver requestClientInfoResolver;
    private final TokenBucketManager tokenBucketManager = new TokenBucketManager();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String rateLimitKey = getRateLimitKey(exchange);
        if (tokenBucketManager.getBucket(rateLimitKey).tryConsume(1)) {
            return chain.filter(exchange);
        }

        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return FilterOrders.getOrder(this.getClass());
    }

    private String getRateLimitKey(ServerWebExchange exchange) {
        ClientInfo clientInfo = requestClientInfoResolver.resolveClientInfo(exchange);
        return String.format("Rate_limit_key::%s::%s::%s",
                clientInfo.getHost(), clientInfo.getBrowser(), clientInfo.getOs());
    }

    private static class TokenBucketManager {

        private static final long DEFAULT_REFILL_RATE = 10;
        private static final long DEFAULT_CAPACITY = 100;
        private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();

        public TokenBucket getBucket(String key) {
            return getBucket(key, DEFAULT_CAPACITY, DEFAULT_REFILL_RATE);
        }

        public TokenBucket getBucket(String key, long capacity, long refillRate) {
            return buckets.computeIfAbsent(key, k -> new TokenBucket(capacity, refillRate));
        }
    }

    private static class TokenBucket {

        private final long capacity;
        private final long refillRate;
        private final AtomicLong tokens;
        private final AtomicLong lastRefillTime;

        public TokenBucket(long capacity, long refillRate) {
            this.capacity = capacity;
            this.refillRate = refillRate;
            this.tokens = new AtomicLong(capacity);
            this.lastRefillTime = new AtomicLong(System.currentTimeMillis());
        }

        public synchronized boolean tryConsume(long tokensRequested) {

            refill();

            long currentTokens = tokens.get();
            if (currentTokens >= tokensRequested) {
                tokens.set(currentTokens - tokensRequested);
                return true;
            }

            return false;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            long lastRefill = lastRefillTime.get();
            long timePassed = now - lastRefill;

            if (timePassed > 0) {
                long tokensToAdd = (timePassed * refillRate) / 1000;
                if (tokensToAdd > 0 && lastRefillTime.compareAndSet(lastRefill, now)) {
                    long currentTokens = tokens.get();
                    long newTokens = Math.min(capacity, currentTokens + tokensToAdd);
                    tokens.set(newTokens);
                }
            }
        }
    }

}

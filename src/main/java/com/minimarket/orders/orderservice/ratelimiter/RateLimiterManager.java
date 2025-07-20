package com.minimarket.orders.orderservice.ratelimiter;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages rate limiters for different accounts to control the frequency of requests.
 * This component creates and caches {@link RateLimiter} instances per account ID,
 * using a predefined configuration that limits requests to 2 per hour with no timeout.
 */
@Component
public class RateLimiterManager {

    private final Map<String, RateLimiter> limiters = new ConcurrentHashMap<>();

    private final RateLimiterConfig config = RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofHours(1))
            .limitForPeriod(2)
            .timeoutDuration(Duration.ZERO)
            .build();

    /**
     * Retrieves or creates a {@link RateLimiter} for the specified account ID.
     * If a rate limiter for the account ID does not exist, a new one is created
     * with the predefined configuration and cached for future use.
     *
     * @param accountId the ID of the account for which to retrieve the rate limiter
     * @return the {@link RateLimiter} associated with the account ID
     */
    public RateLimiter getLimiter(String accountId) {
        return limiters.computeIfAbsent(accountId, id -> RateLimiter.of(id, config));
    }
}
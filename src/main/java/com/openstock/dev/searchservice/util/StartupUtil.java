package com.openstock.dev.searchservice.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.openstock.dev.searchservice.constants.Constants.PRODUCT_CACHE_PREFIX;

@Slf4j
@RequiredArgsConstructor
@Component
public class StartupUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Clears all cache entries with the specified prefix asynchronously on application startup.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void clearCacheOnStartup() {
        log.info("Application startup: initiating cache clear for prefix {}", PRODUCT_CACHE_PREFIX);
        evictCacheGroupDirectly();
    }

    public void evictCacheGroupDirectly() {
        String pattern = PRODUCT_CACHE_PREFIX + "*";
        log.info("Evicting all Redis cache entries with pattern: {}", pattern);
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("Cleared {} cache entries with prefix: {}", keys.size(), PRODUCT_CACHE_PREFIX);
        } else {
            log.info("No cache entries found with prefix: {}", PRODUCT_CACHE_PREFIX);
        }
    }
}

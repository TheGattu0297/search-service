package com.openstock.dev.searchservice.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.openstock.dev.searchservice.constants.Constants.PRODUCT_CACHE_PREFIX;

@Slf4j
@RequiredArgsConstructor
@Service
public class CacheService {

    private final CacheManager cacheManager;

    // Evict all entries for the specific cache group (using prefix)
    @Async  // This method will run asynchronously
    public void evictCacheGroupAsync() {
        log.info("Asynchronously evicting all cache entries for group: {}", PRODUCT_CACHE_PREFIX);
        for (String cacheName : cacheManager.getCacheNames()) {
            if (cacheName.startsWith(PRODUCT_CACHE_PREFIX)) {
                Cache cache = cacheManager.getCache(cacheName);
                if (cache != null) {
                    cache.clear();
                    log.info("Cleared cache: {}", cacheName);
                }
            }
        }
    }

    // Helper methods to evict specific cache entries based on the product and attribute
    public void evictProductCache(String productId) {
        Cache productCache = cacheManager.getCache(PRODUCT_CACHE_PREFIX + "Product");
        if (productCache != null) {
            productCache.evict(productId);
        }
    }

    public void evictAllProductCache() {
        Cache allProductsCache = cacheManager.getCache(PRODUCT_CACHE_PREFIX + "AllProducts");
        if (allProductsCache != null) {
            allProductsCache.clear();
        }
    }

    public void evictCountryCache(String country) {
        Cache countryCache = cacheManager.getCache(PRODUCT_CACHE_PREFIX + "ProductsByCountry");
        if (countryCache != null) {
            countryCache.evict(country);
        }
    }

    public void evictTypeCache(String type) {
        Cache typeCache = cacheManager.getCache(PRODUCT_CACHE_PREFIX + "ProductsByType");
        if (typeCache != null) {
            typeCache.evict(type);
        }
    }

    public void evictSubTypeCache(String subType) {
        Cache subTypeCache = cacheManager.getCache(PRODUCT_CACHE_PREFIX + "ProductsBySubType");
        if (subTypeCache != null) {
            subTypeCache.evict(subType);
        }
    }

    public void evictRegionCache(String region) {
        Cache regionCache = cacheManager.getCache(PRODUCT_CACHE_PREFIX + "ProductsByRegion");
        if (regionCache != null) {
            regionCache.evict(region);
        }
    }

    public void evictSubRegionCache(String subRegion) {
        Cache subRegionCache = cacheManager.getCache(PRODUCT_CACHE_PREFIX + "ProductsBySubRegion");
        if (subRegionCache != null) {
            subRegionCache.evict(subRegion);
        }
    }

    public void evictDenominationCache(String denomination) {
        Cache denominationCache = cacheManager.getCache(PRODUCT_CACHE_PREFIX + "ProductsByDenomination");
        if (denominationCache != null) {
            denominationCache.evict(denomination);
        }
    }

    public void evictProducerCache(String producer) {
        Cache producerCache = cacheManager.getCache(PRODUCT_CACHE_PREFIX + "ProductsByProducer");
        if (producerCache != null) {
            producerCache.evict(producer);
        }
    }

    public void evictNameCache(String name) {
        Cache nameCache = cacheManager.getCache(PRODUCT_CACHE_PREFIX + "ProductsByName");
        if (nameCache != null) {
            nameCache.evict(name);
        }
    }

    public void evictVarietyCache(String variety) {
        Cache varietyCache = cacheManager.getCache(PRODUCT_CACHE_PREFIX + "ProductsByVariety");
        if (varietyCache != null) {
            varietyCache.evict(variety);
        }
    }

    public void evictAlcoholCache(String alcoholPercentage) {
        Cache alcoholCache = cacheManager.getCache(PRODUCT_CACHE_PREFIX + "ProductsByAlcoholPercentage");
        if (alcoholCache != null) {
            alcoholCache.evict(alcoholPercentage);
        }
    }

    public void evictVintageCache(String vintage) {
        Cache vintageCache = cacheManager.getCache(PRODUCT_CACHE_PREFIX + "ProductsByVintage");
        if (vintageCache != null) {
            vintageCache.evict(vintage);
        }
    }
}

package com.openstock.dev.searchservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CacheService {

    private final CacheManager cacheManager;

    @CacheEvict(value = "*", allEntries = true)
    public void removeAllCachedData() {
      log.info("Removing all cached data!");
    }

    // Helper methods to evict caches based on the product and attribute
    public void evictProductCache(String productId) {
        Cache productCache = cacheManager.getCache("Product");
        if (productCache != null) {
            productCache.evict(productId); // Evict individual product cache
        }

        Cache allProductsCache = cacheManager.getCache("AllProducts");
        if (allProductsCache != null) {
            allProductsCache.clear(); // Evict the "getAllProducts" cache
        }
    }

    public void evictCountryCache(String country) {
        Cache countryCache = cacheManager.getCache("ProductsByCountry");
        if (countryCache != null) {
            countryCache.evict(country);
        }
    }

    public void evictTypeCache(String type) {
        Cache typeCache = cacheManager.getCache("ProductsByType");
        if (typeCache != null) {
            typeCache.evict(type);
        }
    }

    public void evictSubTypeCache(String subType) {
        Cache subTypeCache = cacheManager.getCache("ProductsBySubType");
        if (subTypeCache != null) {
            subTypeCache.evict(subType);
        }
    }

    public void evictRegionCache(String region) {
        Cache regionCache = cacheManager.getCache("ProductsByRegion");
        if (regionCache != null) {
            regionCache.evict(region);
        }
    }

    public void evictSubRegionCache(String subRegion) {
        Cache subRegionCache = cacheManager.getCache("ProductsBySubRegion");
        if (subRegionCache != null) {
            subRegionCache.evict(subRegion);
        }
    }

    public void evictDenominationCache(String denomination) {
        Cache denominationCache = cacheManager.getCache("ProductsByDenomination");
        if (denominationCache != null) {
            denominationCache.evict(denomination);
        }
    }

    public void evictProducerCache(String producer) {
        Cache producerCache = cacheManager.getCache("ProductsByProducer");
        if (producerCache != null) {
            producerCache.evict(producer);
        }
    }

    public void evictNameCache(String name) {
        Cache nameCache = cacheManager.getCache("ProductsByName");
        if (nameCache != null) {
            nameCache.evict(name);
        }
    }

    public void evictVarietyCache(String variety) {
        Cache varietyCache = cacheManager.getCache("ProductsByVariety");
        if (varietyCache != null) {
            varietyCache.evict(variety);
        }
    }

    public void evictAlcoholCache(String alcoholPercentage) {
        Cache alcoholCache = cacheManager.getCache("ProductsByAlcoholPercentage");
        if (alcoholCache != null) {
            alcoholCache.evict(alcoholPercentage);
        }
    }

    public void evictVintageCache(String vintage) {
        Cache vintageCache = cacheManager.getCache("ProductsByVintage");
        if (vintageCache != null) {
            vintageCache.evict(vintage);
        }
    }
}

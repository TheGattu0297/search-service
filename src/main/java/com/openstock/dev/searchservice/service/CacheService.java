package com.openstock.dev.searchservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CacheService {

    @CacheEvict(value = "*", allEntries = true)
    public void removeAllCachedData() {
      log.info("Removing all cached data");
    }
}

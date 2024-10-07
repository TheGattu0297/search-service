package com.openstock.dev.searchservice.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/searchCache")
public class CacheController {

    private final CacheService cacheService;

    // Controller to clear all cached data
    @DeleteMapping("/invalidate-all")
    public ResponseEntity<String> invalidateAllCaches() {
        cacheService.evictCacheGroupAsync();
        return ResponseEntity.ok("All Search-Service cache removed!"); // HTTP 204 No Content
    }
}

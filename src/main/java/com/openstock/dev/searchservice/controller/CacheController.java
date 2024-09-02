package com.openstock.dev.searchservice.controller;

import com.openstock.dev.searchservice.service.CacheService;
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

    @DeleteMapping("/invalidate-all")
    public ResponseEntity<Void> invalidateAllCaches() {
        cacheService.removeAllCachedData();
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}

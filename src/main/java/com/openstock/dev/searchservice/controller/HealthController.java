package com.openstock.dev.searchservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping(value = "/check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok().build();
    }
}

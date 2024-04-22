package com.openstock.dev.searchservice.controller;

import com.openstock.dev.searchservice.model.Product;
import com.openstock.dev.searchservice.service.ElasticSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/search/bar")
public class SearchController {

    private final ElasticSearchService elasticSearchService;

    public SearchController(ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @GetMapping("/productByName/{name}")
    public List<Product> findProductsByName(@PathVariable("name") String name) {
        return elasticSearchService.findProductsByName(name);
    }

    @GetMapping("/fetchSuggestions/{keyword}")
    public List<Product> findSuggestionsByKeyword(@PathVariable("keyword") String keyword) {
        return elasticSearchService.fetchSuggestions(keyword);
    }
}

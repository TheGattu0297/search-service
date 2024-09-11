package com.openstock.dev.searchservice.controller;

import com.openstock.dev.searchservice.entity.Product;
import com.openstock.dev.searchservice.service.ElasticSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping(value = "/search")
public class SearchController {

    private final ElasticSearchService elasticSearchService;

    @GetMapping("/getAll")
    public Iterable<Product> getAllProducts() {
        return elasticSearchService.getAllProducts();
    }

    @GetMapping("/getById/{productId}")
    public Product getProductById(@PathVariable String productId) {
        return elasticSearchService.getProductById(productId);
    }

    @GetMapping("/master/{master}")
    public List<Product> getProductByMaster(@PathVariable("master") String master) {
        return elasticSearchService.getProductByMaster(master);
    }

    @GetMapping("/country/{country}")
    public List<Product> getProductByCountry(@PathVariable("country") String country) {
        return elasticSearchService.getProductsByCountry(country);
    }

    @GetMapping("/type/{type}")
    public List<Product> getProductsByType(@PathVariable("type") String type) {
        return elasticSearchService.getProductsByType(type);
    }

    @GetMapping("/subType/{subType}")
    public List<Product> getProductsBySubType(@PathVariable("subType") String subType) {
        return elasticSearchService.getProductsBySubType(subType);
    }

    @GetMapping("/reg/{region}")
    public List<Product> getProductsByRegion(@PathVariable("region") String region) {
        return elasticSearchService.getProductsByReg(region);
    }

    @GetMapping("/sub/{subRegion}")
    public List<Product> getProductsBySubRegion(@PathVariable("subRegion") String subRegion) {
        return elasticSearchService.getProductsBySub(subRegion);
    }

    @GetMapping("/deno/{denomination}")
    public List<Product> getProductsByDenomination(@PathVariable("denomination") String denomination) {
        return elasticSearchService.getProductsByDeno(denomination);
    }

    @GetMapping("/prod/{producer}")
    public List<Product> getProductsByProducer(@PathVariable("producer") String producer) {
        return elasticSearchService.getProductsByProd(producer);
    }

    @GetMapping("/name/{name}")
    public List<Product> getProductsByName(@PathVariable("name") String name) {
        return elasticSearchService.getProductsByName(name);
    }

    @GetMapping("/variety/{variety}")
    public List<Product> getProductsByVariety(@PathVariable("variety") String variety) {
        return elasticSearchService.getProductsByVariety(variety);
    }

    @GetMapping("/alc/{alcoholPercentage}")
    public List<Product> getProductsByAlcoholPercentage(@PathVariable("alcoholPercentage") String alcoholPercentage) {
        String alc = alcoholPercentage + "%";
        return elasticSearchService.getProductsByAlc(alc);
    }

    @GetMapping("/vintage/{vintage}")
    public List<Product> getProductsByVintage(@PathVariable("vintage") String vintage) {
        return elasticSearchService.getProductsByVintage(vintage);
    }

    @GetMapping("/{keyword}")
    public List<Product> findSuggestionsByKeyword(@PathVariable("keyword") String keyword) {
        return elasticSearchService.fetchSuggestions(keyword);
    }
}

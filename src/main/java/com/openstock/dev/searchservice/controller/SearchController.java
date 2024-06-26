package com.openstock.dev.searchservice.controller;

import com.openstock.dev.searchservice.model.Product;
import com.openstock.dev.searchservice.service.ElasticSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/search")
public class SearchController {

    private final ElasticSearchService elasticSearchService;

    public SearchController(ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @GetMapping(value = "/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/save")
    public ResponseEntity<String> addProduct (@RequestBody Product product){
        elasticSearchService.saveProduct(product);
        return ResponseEntity.ok("Product inserted successfully!");
    }

    @PostMapping("/saveAll")
    public ResponseEntity<String> addProducts (@RequestBody List<Product> products){
        elasticSearchService.saveProducts(products);
        return ResponseEntity.ok("Products inserted successfully!");
    }

    @GetMapping("/getAll")
    public Iterable<Product> getAllProducts (){
        return elasticSearchService.getProducts();
    }

    @GetMapping("/getAllInRange")
    public Iterable<Product> getAllProductsInRange (@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "500") int size){
        int from = page * size; // Calculate starting index based on page and size
        return elasticSearchService.getProductsInRange(from, size);
    }

    @GetMapping("/getById/{productId}")
    public Product getProductById (@PathVariable String productId){
        return elasticSearchService.getProductById(productId);
    }

    @GetMapping("/master/{master}")
    public List<Product> getProductByMaster (@PathVariable String master){
        return elasticSearchService.getProductByMaster(master);
    }

    @GetMapping("/country/{country}")
    public List<Product> getProductByCountry(@PathVariable("country") String country) {
        return elasticSearchService.findByCountry(country);
    }

    @GetMapping("/type/{type}")
    public List<Product> getProductsByType(@PathVariable("type") String type) {
        return elasticSearchService.findByType(type);
    }

    @GetMapping("/subType/{subType}")
    public List<Product> getProductsBySubType(@PathVariable("subType") String subType) {
        return elasticSearchService.findBySubType(subType);
    }

    @GetMapping("/reg/{region}")
    public List<Product> getProductsByRegion(@PathVariable("region") String region) {
        return elasticSearchService.findByReg(region);
    }

    @GetMapping("/sub/{subRegion}")
    public List<Product> getProductsBySubRegion(@PathVariable("subRegion") String subRegion) {
        return elasticSearchService.findBySub(subRegion);
    }

    @GetMapping("/deno/{denomination}")
    public List<Product> getProductsByDenomination(@PathVariable("denomination") String denomination) {
        return elasticSearchService.findByDeno(denomination);
    }

    @GetMapping("/prod/{producer}")
    public List<Product> getProductsByProducer(@PathVariable("producer") String producer) {
        return elasticSearchService.findByProd(producer);
    }

    @GetMapping("/name/{name}")
    public List<Product> getProductsByName(@PathVariable("name") String name) {
        return elasticSearchService.findByName(name);
    }

    @GetMapping("/variety/{variety}")
    public List<Product> getProductsByVariety(@PathVariable("variety") String variety) {
        return elasticSearchService.findByVariety(variety);
    }

    @GetMapping("/alc/{alcoholPercentage}")
    public List<Product> getProductsByAlcoholPercentage(@PathVariable("alcoholPercentage") String alcoholPercentage) {
        return elasticSearchService.findByAlc(alcoholPercentage);
    }

    @GetMapping("/vintage/{vintage}")
    public List<Product> getProductsByVintage(@PathVariable("vintage") String vintage) {
        return elasticSearchService.findByVintage(vintage);
    }

    @DeleteMapping("/deleteAll")
    public String deleteProducts() {
        elasticSearchService.deleteAll();
        return "All Products in Elastic deleted !!";
    }

    @GetMapping("/{keyword}")
    public List<Product> findSuggestionsByKeyword(@PathVariable("keyword") String keyword) {
        return elasticSearchService.fetchSuggestions(keyword);
    }
}

package com.openstock.dev.searchservice.controller;

import com.openstock.dev.searchservice.model.Product;
import com.openstock.dev.searchservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/search/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> addProductById(@RequestBody Product product){
        productService.saveProduct(product);
        return ResponseEntity.ok("Product inserted successfully!");
    }

    @PostMapping("/saveAll")
    public ResponseEntity<String> addProducts (@RequestBody List<Product> products){
        productService.saveProducts(products);
        return ResponseEntity.ok("Products inserted successfully!");
    }

    @GetMapping("/getAll")
    public Iterable<Product> getAllProducts(){
        return productService.getProducts();
    }

    @GetMapping("/getById/{productId}")
    public Product getProduct(@PathVariable String productId){
        return productService.getProductById(productId);
    }

    @GetMapping("/country/{country}")
    public List<Product> getProductByCountry(@PathVariable("country") String country) {
        return productService.findByCountry(country);
    }

    @GetMapping("/type/{type}")
    public List<Product> getProductsByType(@PathVariable("type") String type) {
        return productService.findByType(type);
    }

    @GetMapping("/subType/{subType}")
    public List<Product> getProductsBySubType(@PathVariable("subType") String subType) {
        return productService.findBySubType(subType);
    }

    @GetMapping("/region/{region}")
    public List<Product> getProductsByRegion(@PathVariable("region") String region) {
        return productService.findByReg(region);
    }

    @GetMapping("/subRegion/{subRegion}")
    public List<Product> getProductsBySubRegion(@PathVariable("subRegion") String subRegion) {
        return productService.findBySub(subRegion);
    }

    @GetMapping("/denomination/{denomination}")
    public List<Product> getProductsByDenomination(@PathVariable("denomination") String denomination) {
        return productService.findByDeno(denomination);
    }

    @GetMapping("/producer/{producer}")
    public List<Product> getProductsByProducer(@PathVariable("producer") String producer) {
        return productService.findByProd(producer);
    }

    @GetMapping("/name/{name}")
    public List<Product> getProductsByName(@PathVariable("name") String name) {
        return productService.findByName(name);
    }

    @GetMapping("/variety/{variety}")
    public List<Product> getProductsByVariety(@PathVariable("variety") String variety) {
        return productService.findByVariety(variety);
    }

    @GetMapping("/alcoholPercentage/{alcoholPercentage}")
    public List<Product> getProductsByAlcoholPercentage(@PathVariable("alcoholPercentage") String alcoholPercentage) {
        return productService.findByAlc(alcoholPercentage);
    }

    @GetMapping("/vintage/{vintage}")
    public List<Product> getProductsByVintage(@PathVariable("vintage") String vintage) {
        return productService.findByVintage(vintage);
    }

    @DeleteMapping("/deleteAll")
    public String deleteProducts() {
        productService.deleteAll();
        return "All Products in Elastic deleted !!";
    }
}

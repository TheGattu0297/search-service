package com.openstock.dev.searchservice.controller;

import com.openstock.dev.searchservice.model.Product;
import com.openstock.dev.searchservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/search/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/saveAll")
    public List<Product> addProducts (@RequestBody List<Product> products){
        return productService.saveProducts(products);
    }

    @PostMapping("/save")
    public Product addProductById(@RequestBody Product product){
        return productService.saveProduct(product);
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

    @DeleteMapping("/deleteAll")
    public String deleteProducts() {
        productService.deleteAll();
        return "All Products in Elastic deleted !!";
    }
}

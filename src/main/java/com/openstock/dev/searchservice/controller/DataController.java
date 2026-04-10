package com.openstock.dev.searchservice.controller;

import com.openstock.dev.searchservice.dto.ProductBoostRequestDTO;
import com.openstock.dev.searchservice.entity.Product;
import com.openstock.dev.searchservice.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/data")
public class DataController {

    private final DataService dataService;

    @PostMapping("/save")
    public ResponseEntity<String> addProduct (@RequestBody Product product){
        dataService.saveProduct(product);
        return ResponseEntity.ok("Product inserted successfully!");
    }

    @PostMapping("/saveAll")
    public ResponseEntity<String> addProducts (@RequestBody List<Product> products){
        dataService.saveProducts(products);
        return ResponseEntity.ok("Products inserted successfully!");
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteProducts() {
        dataService.deleteAll();
        return ResponseEntity.ok("All Products in Elastic Cluster deleted !!");
    }

    @PostMapping("/addBoost")
    public ResponseEntity<String> addBoost(@RequestBody ProductBoostRequestDTO request) {
        try {
            dataService.addBoostToProduct(request);
            return ResponseEntity.ok("Boosting information updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update boosting information: " + e.getMessage());
        }
    }

    @PostMapping("/removeBoost")
    public ResponseEntity<String> removeBoost(@RequestParam String productId) {
        try {
            dataService.removeBoostFromProduct(productId);
            return ResponseEntity.ok("Boosting removed successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to remove boosting: " + e.getMessage());
        }
    }
}

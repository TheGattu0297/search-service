package com.openstock.dev.searchservice.service;

import com.openstock.dev.searchservice.model.Product;
import com.openstock.dev.searchservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService (ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Persist the individual Product entity in the Elasticsearch cluster
     */
    public void saveProduct(Product product) {
        try {
            productRepository.save(product);
        } catch (Exception e) {
            log.info("Error saving product", e);
        }
    }

    /**
     * Bulk-save the Products in the Elasticsearch cluster
     */
    public void saveProducts(List<Product> productList) {
        try{
            productRepository.saveAll(productList);
        } catch (Exception e){
            log.info("Error saving products", e);
        }
    }

    public Iterable<Product> getProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(String productId){
        return productRepository.findById(productId).orElse(null);
    }

    public List<Product> findByCountry(String country) {
        return productRepository.findByCountry(country);
    }

    public List<Product> findByType(String type) {
        return productRepository.findByType(type);
    }

    public List<Product> findBySubType(String subType) {
        return productRepository.findBySubType(subType);
    }

    public List<Product> findByReg(String region) {
        return productRepository.findByReg(region);
    }

    public List<Product> findBySub(String subRegion) {
        return productRepository.findBySub(subRegion);
    }

    public List<Product> findByDeno(String denomination) {
        return productRepository.findByDeno(denomination);
    }

    public List<Product> findByProd (String producer) {
        return productRepository.findByProd(producer);
    }

    public List<Product> findByName (String name) {
        return productRepository.findByName(name);
    }

    public List<Product> findByVariety (String variety) {
        return productRepository.findByVariety(variety);
    }

    public List<Product> findByAlc (String alcoholPercentage) {
        return productRepository.findByAlc(alcoholPercentage);
    }

    public List<Product> findByVintage (String vintage) {
        return productRepository.findByVintage(vintage);
    }

    public void deleteAll() {productRepository.deleteAll();}
}

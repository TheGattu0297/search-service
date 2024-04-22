package com.openstock.dev.searchservice.service;

import com.openstock.dev.searchservice.model.Product;
import com.openstock.dev.searchservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
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

    public List<Product> findByRegion(String region) {
        return productRepository.findByRegion(region);
    }

    public List<Product> findBySubRegion(String subRegion) {
        return productRepository.findBySubRegion(subRegion);
    }

    public List<Product> findByDenomination(String denomination) {
        return productRepository.findByDenomination(denomination);
    }

    public List<Product> findByProducer (String producer) {
        return productRepository.findByProducer(producer);
    }

    public List<Product> findByName (String name) {
        return productRepository.findByName(name);
    }

    public List<Product> findByVariety (String variety) {
        return productRepository.findByVariety(variety);
    }

    public List<Product> findByAlcoholPercentage (String alcoholPercentage) {
        return productRepository.findByAlcoholPercentage(alcoholPercentage);
    }

    public List<Product> findByVintage (String vintage) {
        return productRepository.findByVintage(vintage);
    }

    public List<Product> findByInfo (String info) {
        return productRepository.findByInfo(info);
    }

    public void deleteAll() {productRepository.deleteAll();}
}

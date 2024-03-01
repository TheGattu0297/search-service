package com.openstock.dev.searchservice.service;

import com.openstock.dev.searchservice.model.Product;
import com.openstock.dev.searchservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private final ElasticsearchOperations elasticsearchOperations;


    /**
     * Persist the individual Product entity in the Elasticsearch cluster
     */
    public Product saveProduct(Product product) {
        return elasticsearchOperations.save(product);
    }
    /**
     * Bulk-save the Products in the Elasticsearch cluster
     */
    public List<Product> saveProducts(Iterable<Product> productList) {
        List<Product> products = new ArrayList<>();
        elasticsearchOperations.save(productList).forEach(products::add);
        return products;
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

    public void deleteAll() {productRepository.deleteAll();}
}

package com.openstock.dev.searchservice.repository;

import com.openstock.dev.searchservice.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends ElasticsearchRepository<Product, String> {

    List<Product> findByProductIDIn(List<String> productIds); // Custom query to find by multiple IDs
}

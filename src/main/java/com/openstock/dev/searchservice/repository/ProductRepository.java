package com.openstock.dev.searchservice.repository;

import com.openstock.dev.searchservice.model.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends ElasticsearchRepository<Product,String> {

    List<Product> findByCountry(String country);

    List<Product> findByType(String type);

    List<Product> findByRegion (String region);
}

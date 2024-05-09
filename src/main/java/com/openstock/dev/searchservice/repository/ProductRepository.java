package com.openstock.dev.searchservice.repository;

import com.openstock.dev.searchservice.model.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends ElasticsearchRepository<Product,String> {

    List<Product> findByCountry(String country);

    List<Product> findByType(String type);

    List<Product> findBySubType (String subType);

    List<Product> findByReg (String region);

    List<Product> findBySub(String subRegion);

    List<Product> findByDeno (String denomination);

    List<Product> findByProd (String producer);

    List<Product> findByName (String name);

    List<Product> findByVariety (String variety);

    List<Product> findByAlc(String alcoholPercentage);

    List<Product> findByVintage (String vintage);
}

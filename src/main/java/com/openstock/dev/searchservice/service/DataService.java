package com.openstock.dev.searchservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import com.openstock.dev.searchservice.constants.HelperType;
import com.openstock.dev.searchservice.entity.Product;
import com.openstock.dev.searchservice.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.openstock.dev.searchservice.constants.Constants.DB_CALL;
import static com.openstock.dev.searchservice.constants.Constants.ELASTIC_INDEX;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataService {

    private final ElasticsearchClient elasticsearchClient;
    private final DataRepository dataRepository;

    /**
     * Persist the individual Product entity in the Elasticsearch cluster
     */
    public void saveProduct(Product product) {
        try {
            elasticsearchClient.index(i -> i
                    .index(ELASTIC_INDEX)
                    .id(product.getProductID())  // Set the ID here
                    .document(product)
            );
        } catch (Exception e) {
            log.error("Error saving product: {}", e.getMessage(), e);
        }
    }

    /**
     * Bulk-save the Products in the Elasticsearch cluster
     */
    public void saveProducts(List<Product> productList) {
        try {
            BulkRequest.Builder bulkRequest = new BulkRequest.Builder();
            for (Product product : productList) {
                bulkRequest.operations(op -> op
                        .index(idx -> idx
                                .index(ELASTIC_INDEX)
                                .id(product.getProductID())  // Set the ID here
                                .document(product)
                        )
                );
            }
            BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequest.build());
            if (bulkResponse.errors()) {
                log.error("Errors occurred during bulk save: {}", bulkResponse.items().size());
            }
        } catch (Exception e) {
            log.error("Error saving products: {}", e.getMessage(), e);
        }
    }

    /**
     * Delete all the Products in the Elasticsearch cluster
     */
    public void deleteAll() {
        try {
            elasticsearchClient.deleteByQuery(d -> d
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .matchAll(m -> m)
                    )
            );
        } catch (Exception e) {
            log.error("Error deleting all products: {}", e.getMessage(), e);
        }
    }

    /**
     * Update Products by Helper Values in the Elasticsearch cluster
     */
    public void updateProducts(HelperType helperType, Set<String> productIds, String updatedValue, String updatedFlag) {

        // Fetch the products by product IDs
        List<String> productIdList = new ArrayList<>(productIds);
        List<Product> products = getProductsByIds(productIdList);  // Assume findProductsByIds is defined

        if (products.isEmpty()) {
            log.warn("No products found for provided product IDs: {}", productIds);
            return;
        }

        // Update the relevant field in each product
        products.forEach(product -> {
            switch (helperType) {
                case COUNTRY:
                    product.setCountry(updatedValue);
                    if (updatedFlag != null) {
                        product.setCountryFlag(updatedFlag); // Country-specific flag update
                    }
                    break;
                case TYPE:
                    product.setType(updatedValue);
                    break;
                case SUB_TYPE:
                    product.setSubType(updatedValue);
                    break;
                case REGION:
                    product.setReg(updatedValue);
                    break;
                case SUB_REGION:
                    product.setSub(updatedValue);
                    break;
                case DENOMINATION:
                    product.setDeno(updatedValue);
                    break;
                case PRODUCER:
                    product.setProd(updatedValue);
                    break;
                case NAME:
                    product.setName(updatedValue);
                    break;
                case VARIETY:
                    product.setVariety(updatedValue);
                    break;
                case ALCOHOL:
                    product.setAlc(updatedValue);
                    break;
                case VINTAGE:
                    product.setVintage(updatedValue);
                    break;
                case INFO:
                    product.setInfo(updatedValue);
                    break;
                case IMAGE:
                    product.setImg(updatedValue);
                    break;
                default:
                    log.error("Unknown helper type: {}", helperType);
                    break;
            }
        });

        // Save the updated products to Elasticsearch
        saveProducts(products);

        log.info("Updated and saved {} products based on helperType: {}", products.size(), helperType);
    }

    //Helper
    public List<Product> getProductsByIds(List<String> productIds) {
        log.info(DB_CALL);
        try {
            return dataRepository.findByProductIDIn(productIds); // Use the custom query
        } catch (Exception e) {
            log.error("Error retrieving products by IDs: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}

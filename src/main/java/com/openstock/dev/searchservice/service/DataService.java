package com.openstock.dev.searchservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import com.openstock.dev.searchservice.cache.CacheService;
import com.openstock.dev.searchservice.constants.HelperType;
import com.openstock.dev.searchservice.dto.ProductBoostRequestDTO;
import com.openstock.dev.searchservice.entity.Product;
import com.openstock.dev.searchservice.entity.fields.Producer;
import com.openstock.dev.searchservice.exceptions.SearchServiceExceptions;
import com.openstock.dev.searchservice.exceptions.SearchServiceExceptions.ProductNotFoundException;
import com.openstock.dev.searchservice.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.openstock.dev.searchservice.constants.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataService {

    private final ElasticsearchClient elasticsearchClient;
    private final CacheService cacheService;
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
            cacheService.evictAllProductCache();
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
            cacheService.evictAllProductCache();
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
            cacheService.evictAllProductCache();
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
            log.error("No products found for provided product IDs: {}", productIds);
            return;
        }

        // Update the relevant field in each product and evict cache for the specific product and attribute
        products.forEach(product -> {
            cacheService.evictProductCache(product.getProductID());
            cacheService.evictAllProductCache();
            // Update the relevant product fields based on the helperType
            switch (helperType) {
                case COUNTRY:
                    if (updatedValue != null) {
                        product.setCountry(updatedValue);
                    }
                    if (updatedFlag != null) {
                        product.setCountryFlag(updatedFlag);
                    }
                    cacheService.evictCountryCache(product.getCountry());
                    break;
                case TYPE:
                    product.setType(updatedValue);
                    cacheService.evictTypeCache(product.getType());
                    break;
                case SUB_TYPE:
                    product.setSubType(updatedValue);
                    cacheService.evictSubTypeCache(product.getSubType());
                    break;
                case REGION:
                    product.setReg(updatedValue);
                    cacheService.evictRegionCache(product.getReg());
                    break;
                case SUB_REGION:
                    product.setSub(updatedValue);
                    cacheService.evictSubRegionCache(product.getSub());
                    break;
                case DENOMINATION:
                    product.setDeno(updatedValue);
                    cacheService.evictDenominationCache(product.getDeno());
                    break;
                case PRODUCER:
                    if (product.getProd() == null) {
                        product.setProd(new Producer()); // Initialize if null
                    }
                    product.getProd().setProdValue(updatedValue);
                    cacheService.evictProducerCache(product.getProd().getProdValue());
                    break;
                case NAME:
                    product.setName(updatedValue);
                    cacheService.evictNameCache(product.getName());
                    break;
                case VARIETY:
                    product.setVariety(updatedValue);
                    cacheService.evictVarietyCache(product.getVariety());
                    break;
                case ALCOHOL:
                    product.setAlc(updatedValue);
                    cacheService.evictAlcoholCache(product.getAlc());
                    break;
                case VINTAGE:
                    product.setVintage(updatedValue);
                    cacheService.evictVintageCache(product.getVintage());
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

    // Method to add boost to a product
    public void addBoostToProduct(ProductBoostRequestDTO boostRequest) {
        try {
            // Fetch the product by ID from Elasticsearch
            Product product = elasticsearchClient.get(g -> g
                    .index(ELASTIC_INDEX)
                    .id(boostRequest.getProductId()), Product.class).source();

            // Null check for product existence
            if (product == null) {
                throw new SearchServiceExceptions.ProductNotFoundException(PRODUCT_WITH_ID +
                        boostRequest.getProductId() + NOT_FOUND);
            }

            // If the product is already boosted, remove the boost first
            if (Boolean.TRUE.equals(product.getIsBoosted())) {
                throw new IllegalStateException("Product is already boosted. Please remove the existing boost first.");
            }

            // Set boost details and priority (default to 4 if not provided)
            product.setIsBoosted(Boolean.TRUE);
            product.setBoostPriority(boostRequest.getBoostPriority() != null ? boostRequest.getBoostPriority() : 3);

            // Save the updated product back to Elasticsearch
            elasticsearchClient.index(i -> i
                    .index(ELASTIC_INDEX)
                    .id(boostRequest.getProductId())
                    .document(product));

            log.info("Boost added for product {}: isBoosted={}, boostPriority={}",
                    boostRequest.getProductId(), Boolean.TRUE, boostRequest.getBoostPriority());

            // Evict caches for the product group after boost update
            cacheService.evictCacheGroupAsync();

        } catch (Exception e) {
            log.error("Error adding boost for product {}: {}", boostRequest.getProductId(), e.getMessage());
            throw new SearchServiceExceptions.ProductAlreadyBoostedException(boostRequest.getProductId());
        }
    }

    // Method to remove boost from a product
    public void removeBoostFromProduct(String productId) {
        try {
            // Fetch the product by ID from Elasticsearch
            Product product = elasticsearchClient.get(g -> g
                    .index(ELASTIC_INDEX)
                    .id(productId), Product.class).source();

            // Null check for product existence
            if (product == null) {
                throw new ProductNotFoundException(PRODUCT_WITH_ID + productId + NOT_FOUND);
            }

//            // If the product is not boosted, throw an exception
//            if (product.getIsBoosted() == null || !product.getIsBoosted()) {
//                throw new IllegalStateException("Product is not boosted, cannot remove boost.");
//            }

            // If the product is not boosted, throw an exception
            if (!Boolean.TRUE.equals(product.getIsBoosted())) {
                throw new IllegalStateException("Product is not boosted, cannot remove boost.");
            }

            // Reset boost fields to null
            product.setIsBoosted(Boolean.FALSE);
            product.setBoostPriority(null);

            // Save the updated product back to Elasticsearch
            elasticsearchClient.index(i -> i
                    .index(ELASTIC_INDEX)
                    .id(productId)
                    .document(product));

            log.info("Boost removed for product {}", productId);

            // Evict caches if this is not a scheduled task
            cacheService.evictCacheGroupAsync();

        } catch (Exception e) {
            log.error("Error removing boost for product {}: {}", productId, e.getMessage());
            throw new SearchServiceExceptions.ProductNotBoostedException(productId);
        }
    }
}

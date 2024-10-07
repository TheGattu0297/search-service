package com.openstock.dev.searchservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co. elastic. clients. elasticsearch._types. SortOrder;
import com.openstock.dev.searchservice.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.openstock.dev.searchservice.constants.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ElasticSearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Cacheable(key = "#productId", value = PRODUCT_CACHE_PREFIX + "Product", unless = "#result == null")
    public Product getProductById(String productId) {
        log.info(DB_CALL);
        try {
            return elasticsearchClient.get(g -> g
                            .index(ELASTIC_INDEX)
                            .id(productId), Product.class)
                    .source();
        } catch (Exception e) {
            log.error("Error retrieving product by ID: {}", e.getMessage(), e);
            return null;
        }
    }

    @Cacheable(key = "'getAllProducts'", value = PRODUCT_CACHE_PREFIX + "AllProducts", unless = "#result == null")
    public List<Product> getAllProducts() {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q.matchAll(m -> m))
                    .size(10000), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error retrieving all products: {}", e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#master", value = PRODUCT_CACHE_PREFIX + "ProductsByMaster", unless = "#result == null")
    public List<Product> getProductByMaster(String master) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q.match(m -> m.field("master").query(master)))
                    .size(5000), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by master - {}: {}", master, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#country", value = PRODUCT_CACHE_PREFIX + "ProductsByCountry", unless = "#result == null")
    public List<Product> getProductsByCountry(String country) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q.term(t -> t.field("country.raw").value(country)))
                    .size(5000), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by country - {}: {}", country, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#type", value = PRODUCT_CACHE_PREFIX + "ProductsByType", unless = "#result == null")
    public List<Product> getProductsByType(String type) {
        log.info(DB_CALL);
        try {
            // Search for products by type
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .bool(b -> b
                                    .must(m -> m.match(t -> t.field("type.raw").query(type)))  // Match type
                            )
                    )
                    // Sort by isBoosted first (boosted products come first)
                    .sort(sort -> sort
                            .field(f -> f
                                    .field("isBoosted")
                                    .order(SortOrder.Desc)  // isBoosted = true first, then false
                            )
                    )
                    // Then sort by boostPriority (lower number = higher priority)
                    .sort(sort -> sort
                            .field(f -> f
                                    .field("boostPriority")
                                    .order(SortOrder.Asc)  // boostPriority 1, 2, 3
                            )
                    )
                    .size(5000), Product.class);

            // Return the products list
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();

        } catch (Exception e) {
            log.error("Error finding products by type - {}: {}", type, e.getMessage(), e);
            return List.of();
        }
    }


    @Cacheable(key = "#subType", value = PRODUCT_CACHE_PREFIX + "ProductsBySubType", unless = "#result == null")
    public List<Product> getProductsBySubType(String subType) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q.match(t -> t.field("subType.raw").query(subType)))
                    .size(5000), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by subType - {}: {}", subType, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#region", value = PRODUCT_CACHE_PREFIX + "ProductsByRegion", unless = "#result == null")
    public List<Product> getProductsByReg(String region) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q.term(t -> t.field("reg.raw").value(region)))
                    .size(5000), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by region - {}: {}", region, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#subRegion", value = PRODUCT_CACHE_PREFIX + "ProductsBySubRegion", unless = "#result == null")
    public List<Product> getProductsBySub(String subRegion) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q.term(t -> t.field("sub.raw").value(subRegion)))
                    .size(5000), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by sub-region - {}: {}", subRegion, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#denomination", value = PRODUCT_CACHE_PREFIX + "ProductsByDenomination",
            unless = "#result == null")
    public List<Product> getProductsByDeno(String denomination) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q.term(t -> t.field("deno.raw").value(denomination)))
                    .size(5000), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by denomination - {}: {}", denomination, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#producer", value = PRODUCT_CACHE_PREFIX + "ProductsByProducer", unless = "#result == null")
    public List<Product> getProductsByProd(String producer) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q.term(t -> t.field("prod.prodValue.raw").value(producer)))
                    .size(5000), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by producer - {}: {}", producer, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#name", value = PRODUCT_CACHE_PREFIX + "ProductsByName", unless = "#result == null")
    public List<Product> getProductsByName(String name) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q.term(t -> t.field("name.raw").value(name)))
                    .size(5000), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by name - {}: {}", name, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#variety", value = PRODUCT_CACHE_PREFIX + "ProductsByVariety", unless = "#result == null")
    public List<Product> getProductsByVariety(String variety) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q.term(t -> t.field("variety.raw").value(variety)))
                    .size(5000), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by variety - {}: {}", variety, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#alcoholPercentage", value = PRODUCT_CACHE_PREFIX + "ProductsByAlcoholPercentage",
            unless = "#result == null")
    public List<Product> getProductsByAlc(String alcoholPercentage) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q.term(t -> t.field("alc.raw").value(alcoholPercentage)))
                    .size(5000), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by alcohol percentage - {}: {}", alcoholPercentage, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#vintage", value = PRODUCT_CACHE_PREFIX + "ProductsByVintage", unless = "#result == null")
    public List<Product> getProductsByVintage(String vintage) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q.term(t -> t.field("vintage.raw").value(vintage)))
                    .size(5000), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by vintage - {}: {}", vintage, e.getMessage(), e);
            return List.of();
        }
    }

    public List<Product> fetchSuggestions(String searchKeyword) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .bool(b -> b
                                            .should(sh -> sh
                                                    .term(t -> t
                                                            .field("productID")
                                                            .value(searchKeyword)
                                                    )
                                            )
                                            .should(sh -> sh
                                                    .term(t -> t
                                                            .field("master")
                                                            .value(searchKeyword)
                                                    )
                                            )
                                            .should(sh -> sh
                                                    .multiMatch(mm -> mm
                                                            .query(searchKeyword)
                                                            .fields("prod.prodValue^5", "deno^5",
                                                                    "vintage^5", "country^3", "name^3", "reg^3",
                                                                    "type^3", "subType", "sub", "variety", "alc")
                                                            .fuzziness("AUTO")
                                                    )
                                            )
                                    )
                            )
                            .size(100), // Limiting to top 100 results
                    Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error performing search for query - {}: {}", searchKeyword, e.getMessage(), e);
            return List.of();
        }
    }
}

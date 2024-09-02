package com.openstock.dev.searchservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.openstock.dev.searchservice.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.openstock.dev.searchservice.constants.Constants.DB_CALL;
import static com.openstock.dev.searchservice.constants.Constants.ELASTIC_INDEX;

@Slf4j
@RequiredArgsConstructor
@Service
public class ElasticSearchService {

    private final ElasticsearchClient elasticsearchClient;


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

    @Cacheable(key = "#productId", value = "Product", unless = "#result == null")
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

    @Cacheable(key = "'getAllProducts:' + #from + '_' + #size", value = "AllProducts",
            unless = "#result == null")
    public List<Product> getAllProducts(int from, int size) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .matchAll(m -> m)
                            ).from(from)
                            .size(size)
                    , Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error retrieving all products: {}", e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#master + ':' + #from + '_' + #size", value = "ProductsByMaster",
            unless = "#result == null")
    public List<Product> getProductByMaster(String master, int from, int size) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .match(m -> m
                                            .field("master")
                                            .query(master)
                                    )
                            ).from(from)
                            .size(size)
                    , Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by master - {}: {}", master, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#country + ':' + #from + '_' + #size", value = "ProductsByCountry",
            unless = "#result == null")
    public List<Product> getProductsByCountry(String country, int from, int size) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .term(t -> t
                                            .field("country.raw")
                                            .value(country)
                                    )
                            ).from(from)
                            .size(size)
                    , Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by country - {}: {}", country, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#type + ':' + #from + '_' + #size", value = "ProductsByType",
            unless = "#result == null")
    public List<Product> getProductsByType(String type, int from, int size) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .match(t -> t
                                            .field("type.raw")
                                            .query(type)
                                    )
                            ).from(from)
                            .size(size)
                    , Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by type - {}: {}", type, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#subType + ':' + #from + '_' + #size", value = "ProductsBySubType",
            unless = "#result == null")
    public List<Product> getProductsBySubType(String subType, int from, int size) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .match(t -> t
                                            .field("subType.raw")
                                            .query(subType)
                                    )
                            ).from(from)
                            .size(size)
                    , Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by subType - {}: {}", subType, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#region + ':' + #from + '_' + #size", value = "ProductsByRegion",
            unless = "#result == null")
    public List<Product> getProductsByReg(String region, int from, int size) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .term(t -> t
                                            .field("reg.raw")
                                            .value(region)
                                    )
                            ).from(from)
                            .size(size)
                    , Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by region - {}: {}", region, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#subRegion + ':' + #from + '_' + #size", value = "ProductsBySubRegion",
            unless = "#result == null")
    public List<Product> getProductsBySub(String subRegion, int from, int size) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .term(t -> t
                                            .field("sub.raw")
                                            .value(subRegion)
                                    )
                            ).from(from)
                            .size(size)
                    , Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by sub-region - {}: {}", subRegion, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#denomination + ':' + #from + '_' + #size", value = "ProductsByDenomination",
            unless = "#result == null")
    public List<Product> getProductsByDeno(String denomination, int from, int size) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .term(t -> t
                                            .field("deno.raw")
                                            .value(denomination)
                                    )
                            ).from(from)
                            .size(size)
                    , Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by denomination - {}: {}", denomination, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#producer + ':' + #from + '_' + #size", value = "ProductsByProducer",
            unless = "#result == null")
    public List<Product> getProductsByProd(String producer, int from, int size) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .term(t -> t
                                            .field("prod.raw")
                                            .value(producer)
                                    )
                            ).from(from)
                            .size(size)
                    , Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by producer - {}: {}", producer, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#name + ':' + #from + '_' + #size", value = "ProductsByName",
            unless = "#result == null")
    public List<Product> getProductsByName(String name, int from, int size) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .term(t -> t
                                            .field("name.raw")
                                            .value(name)
                                    )
                            ).from(from)
                            .size(size)
                    , Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by name - {}: {}", name, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#variety + ':' + #from + '_' + #size", value = "ProductsByVariety",
            unless = "#result == null")
    public List<Product> getProductsByVariety(String variety, int from, int size) {
        log.info(DB_CALL);
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .term(t -> t
                                            .field("variety.raw")
                                            .value(variety)
                                    )
                            ).from(from)
                            .size(size)
                    , Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by variety - {}: {}", variety, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#alcoholPercentage + ':' + #from + '_' + #size", value = "ProductsByAlcoholPercentage",
            unless = "#result == null")
    public List<Product> getProductsByAlc(String alcoholPercentage, int from, int size) {
        log.info("DB CALL");
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .term(t -> t
                                            .field("alc.raw")
                                            .value(alcoholPercentage)
                                    )
                            ).from(from)
                            .size(size)
                    , Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by alcohol percentage - {}: {}", alcoholPercentage, e.getMessage(), e);
            return List.of();
        }
    }

    @Cacheable(key = "#vintage + ':' + #from + '_' + #size", value = "ProductsByVintage",
            unless = "#result == null")
    public List<Product> getProductsByVintage(String vintage, int from, int size) {
        log.info("DB CALL");
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .term(t -> t
                                            .field("vintage.raw")
                                            .value(vintage)
                                    )
                            ).from(from)
                            .size(size)
                    , Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (Exception e) {
            log.error("Error finding products by vintage - {}: {}", vintage, e.getMessage(), e);
            return List.of();
        }
    }

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

//    public List<Product> fetchSuggestions(String searchKeyword) {
//        try {
//            SearchResponse<Product> response = elasticsearchClient.search(s -> s
//                            .index(ELASTIC_INDEX)
//                            .query(q -> q
//                                    .bool(b -> b
//                                            .should(sh -> sh
//                                                    .term(t -> t
//                                                            .field("productID")
//                                                            .value(searchKeyword)
//                                                    )
//                                            ).should(sh -> sh
//                                                    .term(m -> m
//                                                            .field("master")
//                                                            .value(searchKeyword)
//                                                    )
//                                            )
//                                            .should(sh -> sh
//                                                    .match(m -> m
//                                                            .field("country")
//                                                            .query(searchKeyword)
//                                                            .fuzziness("AUTO")
//                                                    )
//                                            )
//                                            .should(sh -> sh
//                                                    .match(m -> m
//                                                            .field("type")
//                                                            .query(searchKeyword)
//                                                            .fuzziness("AUTO")
//                                                    )
//                                            )
//                                            .should(sh -> sh
//                                                    .match(m -> m
//                                                            .field("subType")
//                                                            .query(searchKeyword)
//                                                            .fuzziness("AUTO")
//                                                    )
//                                            )
//                                            .should(sh -> sh
//                                                    .match(m -> m
//                                                            .field("reg")
//                                                            .query(searchKeyword)
//                                                            .fuzziness("AUTO")
//                                                    )
//                                            )
//                                            .should(sh -> sh
//                                                    .match(m -> m
//                                                            .field("sub")
//                                                            .query(searchKeyword)
//                                                            .fuzziness("AUTO")
//                                                    )
//                                            )
//                                            .should(sh -> sh
//                                                    .match(m -> m
//                                                            .field("deno")
//                                                            .query(searchKeyword)
//                                                            .fuzziness("AUTO")
//                                                    )
//                                            )
//                                            .should(sh -> sh
//                                                    .match(m -> m
//                                                            .field("prod")
//                                                            .query(searchKeyword)
//                                                            .fuzziness("AUTO")
//                                                    )
//                                            )
//                                            .should(sh -> sh
//                                                    .match(m -> m
//                                                            .field("name")
//                                                            .query(searchKeyword)
//                                                            .fuzziness("AUTO")
//                                                    )
//                                            )
//                                            .should(sh -> sh
//                                                    .match(m -> m
//                                                            .field("variety")
//                                                            .query(searchKeyword)
//                                                            .fuzziness("AUTO")
//                                                    )
//                                            )
//                                            .should(sh -> sh
//                                                    .match(m -> m
//                                                            .field("alc")
//                                                            .query(searchKeyword)
//                                                            .fuzziness("AUTO")
//                                                    )
//                                            )
//                                            .should(sh -> sh
//                                                    .match(m -> m
//                                                            .field("vintage")
//                                                            .query(searchKeyword)
//                                                            .fuzziness("AUTO")
//                                                    )
//                                            )
//                                    )
//                            )
//                            .size(100) // Limiting to top 100 results
//                    , Product.class);
//
//            return response.hits().hits().stream()
//                    .map(Hit::source)
//                    .toList();
//        } catch (Exception e) {
//            // Handle exceptions or log error
//            return List.of(); // Return an empty list or handle accordingly
//        }
//    }

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
                                                            .fields("prod^5", "deno^5", "vintage^5",
                                                                    "country^3", "name^3", "reg^3", "type^3",
                                                                    "subType", "sub", "variety", "alc")
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

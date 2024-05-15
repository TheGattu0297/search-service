package com.openstock.dev.searchservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.openstock.dev.searchservice.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.openstock.dev.searchservice.constants.Constants.ELASTIC_INDEX;

@Service
@Slf4j
public class ProductService {

    private final ElasticsearchClient elasticsearchClient;

    public ProductService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

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

    public List<Product> getProducts() {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .matchAll(m -> m)
                    ).size(10000), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving all products: {}", e.getMessage(), e);
            return List.of();
        }
    }

    public Iterable<Product> getProductsInRange(int from, int size) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .matchAll(m -> m)
                            ).from(from) // Starting index
                            .size(size) // Number of records to fetch
                    , Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving all products: {}", e.getMessage(), e);
            return List.of();
        }
    }

    public Product getProductById(String productId) {
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

    public List<Product> getProductByMaster(String keyword) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .match(m -> m
                                    .field("master")
                                    .query(keyword)
                            )
                    ), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error finding products by master - {}: {}", keyword, e.getMessage(), e);
            return List.of();
        }
    }

    public List<Product> findByCountry(String keyword) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .match(m -> m
                                    .field("country")
                                    .query(keyword)
                            )
                    ), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error finding products by country - {}: {}", keyword, e.getMessage(), e);
            return List.of();
        }
    }

    public List<Product> findByType(String keyword) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .match(m -> m
                                    .field("type")
                                    .query(keyword)
                            )
                    ), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error finding products by type - {}: {}", keyword, e.getMessage(), e);
            return List.of();
        }
    }

    public List<Product> findBySubType(String keyword) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .match(m -> m
                                    .field("subType")
                                    .query(keyword)
                            )
                    ), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error finding products by subType - {}: {}", keyword, e.getMessage(), e);
            return List.of();
        }
    }

    public List<Product> findByReg(String keyword) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .match(m -> m
                                    .field("reg")
                                    .query(keyword)
                            )
                    ), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error finding products by region - {}: {}", keyword, e.getMessage(), e);
            return List.of();
        }
    }

    public List<Product> findBySub(String keyword) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .match(m -> m
                                    .field("sub")
                                    .query(keyword)
                            )
                    ), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error finding products by sub-region - {}: {}", keyword, e.getMessage(), e);
            return List.of();
        }
    }

    public List<Product> findByDeno(String keyword) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .match(m -> m
                                    .field("deno")
                                    .query(keyword)
                            )
                    ), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error finding products by denomination - {}: {}", keyword, e.getMessage(), e);
            return List.of();
        }
    }

    public List<Product> findByProd(String keyword) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .match(m -> m
                                    .field("prod")
                                    .query(keyword)
                            )
                    ), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error finding products by producer - {}: {}", keyword, e.getMessage(), e);
            return List.of();
        }
    }

    public List<Product> findByName(String keyword) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .match(m -> m
                                    .field("name")
                                    .query(keyword)
                            )
                    ), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error finding products by name - {}: {}", keyword, e.getMessage(), e);
            return List.of();
        }
    }

    public List<Product> findByVariety(String keyword) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .match(m -> m
                                    .field("variety")
                                    .query(keyword)
                            )
                    ), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error finding products by variety - {}: {}", keyword, e.getMessage(), e);
            return List.of();
        }
    }

    public List<Product> findByAlc(String keyword) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .match(m -> m
                                    .field("alc")
                                    .query(keyword)
                            )
                    ), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error finding products by alcohol percentage - {}: {}", keyword, e.getMessage(), e);
            return List.of();
        }
    }

    public List<Product> findByVintage(String keyword) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .match(m -> m
                                    .field("vintage")
                                    .query(keyword)
                            )
                    ), Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error finding products by vintage - {}: {}", keyword, e.getMessage(), e);
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
}

//    private final ProductRepository productRepository;
//
//    public ProductService (ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }
//
//    /**
//     * Persist the individual Product entity in the Elasticsearch cluster
//     */
//    public void saveProduct(Product product) {
//        try {
//            productRepository.save(product);
//        } catch (Exception e) {
//            log.info("Error saving product", e);
//        }
//    }
//
//    /**
//     * Bulk-save the Products in the Elasticsearch cluster
//     */
//    public void saveProducts(List<Product> productList) {
//        try{
//            productRepository.saveAll(productList);
//        } catch (Exception e){
//            log.info("Error saving products", e);
//        }
//    }
//
//    public Iterable<Product> getProducts(){
//        return productRepository.findAll();
//    }
//
//    public Product getProductById(String productId){
//        return productRepository.findById(productId).orElse(null);
//    }
//
//    public List<Product> findByCountry(String country) {
//        return productRepository.findByCountry(country);
//    }
//
//    public List<Product> findByType(String type) {
//        return productRepository.findByType(type);
//    }
//
//    public List<Product> findBySubType(String subType) {
//        return productRepository.findBySubType(subType);
//    }
//
//    public List<Product> findByReg(String region) {
//        return productRepository.findByReg(region);
//    }
//
//    public List<Product> findBySub(String subRegion) {
//        return productRepository.findBySub(subRegion);
//    }
//
//    public List<Product> findByDeno(String denomination) {
//        return productRepository.findByDeno(denomination);
//    }
//
//    public List<Product> findByProd (String producer) {
//        return productRepository.findByProd(producer);
//    }
//
//    public List<Product> findByName (String name) {
//        return productRepository.findByName(name);
//    }
//
//    public List<Product> findByVariety (String variety) {
//        return productRepository.findByVariety(variety);
//    }
//
//    public List<Product> findByAlc (String alcoholPercentage) {
//        return productRepository.findByAlc(alcoholPercentage);
//    }
//
//    public List<Product> findByVintage (String vintage) {
//        return productRepository.findByVintage(vintage);
//    }
//
//    public void deleteAll() {productRepository.deleteAll();}
//}

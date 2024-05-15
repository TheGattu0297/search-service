package com.openstock.dev.searchservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.openstock.dev.searchservice.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.openstock.dev.searchservice.constants.Constants.ELASTIC_INDEX;

@Service
public class ElasticSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public ElasticSearchService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public List<Product> findProductsByName(final String nameKeyword) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                    .index(ELASTIC_INDEX)
                    .query(q -> q
                            .match(m -> m
                                    .field("name")
                                    .query(nameKeyword)
                                    .analyzer("autocomplete")
                            )
                    ), Product.class);

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Handle exceptions or log error
            return List.of(); // Return an empty list or handle accordingly
        }
    }

    public List<Product> fetchSuggestions(String searchKeyword) {
        try {
            SearchResponse<Product> response = elasticsearchClient.search(s -> s
                            .index(ELASTIC_INDEX)
                            .query(q -> q
                                    .bool(b -> b
                                                    .should(sh -> sh
                                                            .match(m -> m
                                                                    .field("productID")
                                                                    .query(searchKeyword)
                                                            )
                                                    )
                                                    .should(sh -> sh
                                                            .match(m -> m
                                                                    .field("country")
                                                                    .query(searchKeyword)
                                                                    .analyzer("autocomplete")
                                                                    .fuzziness("AUTO")
                                                            )
                                                    )
                                                    .should(sh -> sh
                                                            .match(m -> m
                                                                    .field("type")
                                                                    .query(searchKeyword)
                                                                    .analyzer("autocomplete")
                                                                    .fuzziness("AUTO")
                                                            )
                                                    )
                                                    .should(sh -> sh
                                                            .match(m -> m
                                                                    .field("subType")
                                                                    .query(searchKeyword)
                                                                    .analyzer("autocomplete")
                                                                    .fuzziness("AUTO")
                                                            )
                                                    )
                                                    .should(sh -> sh
                                                            .match(m -> m
                                                                    .field("reg")
                                                                    .query(searchKeyword)
                                                                    .analyzer("autocomplete")
                                                                    .fuzziness("AUTO")
                                                            )
                                                    )
                                                    .should(sh -> sh
                                                            .match(m -> m
                                                                    .field("sub")
                                                                    .query(searchKeyword)
                                                                    .analyzer("autocomplete")
                                                                    .fuzziness("AUTO")
                                                            )
                                                    )
                                                    .should(sh -> sh
                                                            .match(m -> m
                                                                    .field("deno")
                                                                    .query(searchKeyword)
                                                                    .analyzer("autocomplete")
                                                                    .fuzziness("AUTO")
                                                            )
                                                    )
                                                    .should(sh -> sh
                                                            .match(m -> m
                                                                    .field("prod")
                                                                    .query(searchKeyword)
                                                                    .analyzer("autocomplete")
                                                                    .fuzziness("AUTO")
                                                            )
                                                    )
                                                    .should(sh -> sh
                                                            .match(m -> m
                                                                    .field("name")
                                                                    .query(searchKeyword)
                                                                    .analyzer("autocomplete")
                                                                    .fuzziness("AUTO")
                                                            )
                                                    )
                                                    .should(sh -> sh
                                                            .match(m -> m
                                                                    .field("variety")
                                                                    .query(searchKeyword)
                                                                    .analyzer("autocomplete")
                                                                    .fuzziness("AUTO")
                                                            )
                                                    )
                                                    .should(sh -> sh
                                                            .match(m -> m
                                                                    .field("alc")
                                                                    .query(searchKeyword)
                                                                    .analyzer("autocomplete")
                                                                    .fuzziness("AUTO")
                                                            )
                                                    )
                                                    .should(sh -> sh
                                                            .match(m -> m
                                                                    .field("vintage")
                                                                    .query(searchKeyword)
                                                                    .analyzer("autocomplete")
                                                                    .fuzziness("AUTO")
                                                            )
                                                    )
                                            // Add similar fuzzy match clauses for other fields
                                    )
                            )
                            .size(5) // Limiting to top 5 results
                    , Product.class);

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Handle exceptions or log error
            return List.of(); // Return an empty list or handle accordingly
        }
    }

//    public List<Product> fetchSuggestions(String searchKeyword) {
//        try {
//            SearchResponse<Product> response = elasticsearchClient.search(s -> s
//                            .index(ELASTIC_INDEX)
//                            .query(q -> q
//                                    .bool(b -> b
//                                            .should(sh -> sh.term(t -> t.field("productID").value(searchKeyword)))
//                                            .should(sh -> sh.match(m -> m.field("country").query(searchKeyword)
//                                                    .analyzer("autocomplete").fuzziness("AUTO")))
//                                            .should(sh -> sh.match(m -> m.field("type").query(searchKeyword)
//                                                    .analyzer("autocomplete").fuzziness("AUTO")))
//                                            .should(sh -> sh.match(m -> m.field("subType").query(searchKeyword)
//                                                    .analyzer("autocomplete").fuzziness("AUTO")))
//                                            .should(sh -> sh.match(m -> m.field("reg").query(searchKeyword)
//                                                    .analyzer("autocomplete").fuzziness("AUTO")))
//                                            .should(sh -> sh.match(m -> m.field("sub").query(searchKeyword)
//                                                    .analyzer("autocomplete").fuzziness("AUTO")))
//                                            .should(sh -> sh.match(m -> m.field("deno").query(searchKeyword)
//                                                    .analyzer("autocomplete").fuzziness("AUTO")))
//                                            .should(sh -> sh.match(m -> m.field("prod").query(searchKeyword)
//                                                    .analyzer("autocomplete").fuzziness("AUTO")))
//                                            .should(sh -> sh.match(m -> m.field("name").query(searchKeyword)
//                                                    .analyzer("autocomplete").fuzziness("AUTO")))
//                                            .should(sh -> sh.match(m -> m.field("variety").query(searchKeyword)
//                                                    .analyzer("autocomplete").fuzziness("AUTO")))
//                                            .should(sh -> sh.match(m -> m.field("alc").query(searchKeyword)
//                                                    .analyzer("autocomplete").fuzziness("AUTO")))
//                                            .should(sh -> sh.match(m -> m.field("vintage").query(searchKeyword)
//                                                    .analyzer("autocomplete").fuzziness("AUTO")))
//                                    )
//                            )
//                            .size(5), // Limiting to top 5 results
//                    Product.class);
//
//            return response.hits().hits().stream()
//                    .map(Hit::source)
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            // Handle exceptions or log error
//            return List.of(); // Return an empty list or handle accordingly
//        }
//    }

//=====================================================================================================================
//    private final ElasticsearchOperations elasticsearchOperations;
//
//    public ElasticSearchService (ElasticsearchOperations elasticsearchOperations) {
//        this.elasticsearchOperations = elasticsearchOperations;
//    }
//
//    public List<Product> findProductsByName(final String searchKeyword) {
//        // Construct a criteria for text search in the 'name' field
//        Criteria criteria = new Criteria("name").contains(searchKeyword);
//
//        // Create a query using Criteria
//        Query query = new CriteriaQuery(criteria);
//
//        // Execute the search using ElasticsearchOperations
//        SearchHits<Product> searchHits = elasticsearchOperations.search(query, Product.class);
//
//        // Collect results
//        return searchHits.getSearchHits().stream()
//                .map(SearchHit::getContent)
//                .collect(Collectors.toList());
//    }
//
//    public List<Product> fetchSuggestions(String nameKeyword) {
//        Criteria criteria = new Criteria("country").fuzzy(nameKeyword.toLowerCase())
//                .or("type").fuzzy(nameKeyword.toLowerCase())
//                .or("subType").fuzzy(nameKeyword.toLowerCase())
//                .or("reg").fuzzy(nameKeyword.toLowerCase())
//                .or("sub").fuzzy(nameKeyword.toLowerCase())
//                .or("deno").fuzzy(nameKeyword.toLowerCase())
//                .or("prod").fuzzy(nameKeyword.toLowerCase())
//                .or("name").fuzzy(nameKeyword.toLowerCase())
//                .or("variety").fuzzy(nameKeyword.toLowerCase())
//                .or("alc").fuzzy(nameKeyword.toLowerCase())
//                .or("vintage").fuzzy(nameKeyword.toLowerCase());
//
//        CriteriaQuery query = new CriteriaQuery(criteria)
//                .setPageable(PageRequest.of(0, 5)); // Limiting to top 5 results
//
//        // Execute the search
//        SearchHits<Product> searchHits = elasticsearchOperations.search(query, Product.class);
//
//        // Map SearchHits to Product list
//        return searchHits.getSearchHits().stream()
//                .map(SearchHit::getContent)
//                .collect(Collectors.toList());
//    }
}

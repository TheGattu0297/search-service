package com.openstock.dev.searchservice.service;

import com.openstock.dev.searchservice.model.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ElasticSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public ElasticSearchService (ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<Product> findProductsByName(final String searchKeyword) {
        // Construct a criteria for text search in the 'name' field
        Criteria criteria = new Criteria("name").contains(searchKeyword);

        // Create a query using Criteria
        Query query = new CriteriaQuery(criteria);

        // Execute the search using ElasticsearchOperations
        SearchHits<Product> searchHits = elasticsearchOperations.search(query, Product.class);

        // Collect results
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    public List<Product> fetchSuggestions(String nameKeyword) {
        Criteria criteria = new Criteria("country").fuzzy(nameKeyword.toLowerCase())
                .or("type").fuzzy(nameKeyword.toLowerCase())
                .or("region").fuzzy(nameKeyword.toLowerCase())
                .or("subRegion").fuzzy(nameKeyword.toLowerCase())
                .or("denomination").fuzzy(nameKeyword.toLowerCase())
                .or("producer").fuzzy(nameKeyword.toLowerCase())
                .or("name").fuzzy(nameKeyword.toLowerCase())
                .or("variety").fuzzy(nameKeyword.toLowerCase())
                .or("alcoholPercentage").fuzzy(nameKeyword.toLowerCase())
                .or("vintage").fuzzy(nameKeyword.toLowerCase());

        CriteriaQuery query = new CriteriaQuery(criteria)
                .setPageable(PageRequest.of(0, 5)); // Limiting to top 5 results

        // Execute the search
        SearchHits<Product> searchHits = elasticsearchOperations.search(query, Product.class);

        // Map SearchHits to Product list
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}

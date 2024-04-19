package com.openstock.dev.searchservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
@Builder
@Document(indexName = "products")
public class Product {

    @Id
    private String productID;
    private String country;
    private String type;
    private String region;
    private String subRegion;
    private String denomination;
    private String producer;
    private String name;
    private String variety;
    private String alcoholPercentage;
    private String vintage;
    private String info;
}

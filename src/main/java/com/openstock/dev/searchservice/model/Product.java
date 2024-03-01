package com.openstock.dev.searchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

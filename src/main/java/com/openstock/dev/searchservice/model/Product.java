package com.openstock.dev.searchservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Document(indexName = "products")
public class Product {

    @Id
    @Field(type = FieldType.Text, name = "productID")
    private String productID;
    @Field(type = FieldType.Text, name = "country")
    private String country;
    @Field(type = FieldType.Text, name = "type")
    private String type;
    @Field(type = FieldType.Text, name = "region")
    private String region;
    @Field(type = FieldType.Text, name = "subRegion")
    private String subRegion;
    @Field(type = FieldType.Text, name = "denomination")
    private String denomination;
    @Field(type = FieldType.Text, name = "producer")
    private String producer;
    @Field(type = FieldType.Text, name = "name")
    private String name;
    @Field(type = FieldType.Text, name = "variety")
    private String variety;
    @Field(type = FieldType.Text, name = "alcoholPercentage")
    private String alcoholPercentage;
    @Field(type = FieldType.Text, name = "vintage")
    private String vintage;
    @Field(type = FieldType.Text, name = "info")
    private String info;
}

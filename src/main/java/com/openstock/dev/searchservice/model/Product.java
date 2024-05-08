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
    @Field(type = FieldType.Text, name = "subType")
    private String subType;
    @Field(type = FieldType.Text, name = "reg")
    private String reg;
    @Field(type = FieldType.Text, name = "sub")
    private String sub;
    @Field(type = FieldType.Text, name = "deno")
    private String deno;
    @Field(type = FieldType.Text, name = "prod")
    private String prod;
    @Field(type = FieldType.Text, name = "name")
    private String name;
    @Field(type = FieldType.Text, name = "variety")
    private String variety;
    @Field(type = FieldType.Text, name = "alc")
    private String alc;
    @Field(type = FieldType.Text, name = "vintage")
    private String vintage;
    @Field(type = FieldType.Text, name = "info")
    private String info;
    @Field(type = FieldType.Text, name = "img")
    private String img;
}

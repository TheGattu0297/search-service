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
    @Field(type = FieldType.Text, name = "productID", index = true)
    private String productID;
    @Field(type = FieldType.Text, name = "country", index = true)
    private String country;
    @Field(type = FieldType.Text, name = "type", index = true)
    private String type;
    @Field(type = FieldType.Text, name = "subType", index = true)
    private String subType;
    @Field(type = FieldType.Text, name = "reg", index = true)
    private String reg;
    @Field(type = FieldType.Text, name = "sub", index = true)
    private String sub;
    @Field(type = FieldType.Text, name = "deno", index = true)
    private String deno;
    @Field(type = FieldType.Text, name = "prod", index = true)
    private String prod;
    @Field(type = FieldType.Text, name = "name", index = true)
    private String name;
    @Field(type = FieldType.Text, name = "variety", index = true)
    private String variety;
    @Field(type = FieldType.Text, name = "alc", index = true)
    private String alc;
    @Field(type = FieldType.Text, name = "vintage", index = true)
    private String vintage;
    @Field(type = FieldType.Text, name = "info", index = false)
    private String info;
    @Field(type = FieldType.Text, name = "img", index = false)
    private String img;
}

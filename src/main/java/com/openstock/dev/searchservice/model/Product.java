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
    @Field(type = FieldType.Keyword, name = "productID")
    private String productID;
    @Field(type = FieldType.Text, name = "master")
    private String master;
    @Field(type = FieldType.Text, name = "country", analyzer = "autocomplete", searchAnalyzer = "standard")
    private String country;
    @Field(type = FieldType.Text, name = "countryFlag", index = false)
    private String countryFlag;
    @Field(type = FieldType.Text, name = "type", analyzer = "autocomplete", searchAnalyzer = "standard")
    private String type;
    @Field(type = FieldType.Text, name = "subType", analyzer = "autocomplete", searchAnalyzer = "standard")
    private String subType;
    @Field(type = FieldType.Text, name = "reg", analyzer = "autocomplete", searchAnalyzer = "standard")
    private String reg;
    @Field(type = FieldType.Text, name = "sub", analyzer = "autocomplete", searchAnalyzer = "standard")
    private String sub;
    @Field(type = FieldType.Text, name = "deno", analyzer = "autocomplete", searchAnalyzer = "standard")
    private String deno;
    @Field(type = FieldType.Text, name = "prod", analyzer = "autocomplete", searchAnalyzer = "standard")
    private String prod;
    @Field(type = FieldType.Text, name = "name", analyzer = "autocomplete", searchAnalyzer = "standard")
    private String name;
    @Field(type = FieldType.Text, name = "variety", analyzer = "autocomplete", searchAnalyzer = "standard")
    private String variety;
    @Field(type = FieldType.Text, name = "alc", analyzer = "autocomplete", searchAnalyzer = "standard")
    private String alc;
    @Field(type = FieldType.Text, name = "vintage", analyzer = "autocomplete", searchAnalyzer = "standard")
    private String vintage;
    @Field(type = FieldType.Text, name = "info", index = false)
    private String info;
    @Field(type = FieldType.Text, name = "img", index = false)
    private String img;
}

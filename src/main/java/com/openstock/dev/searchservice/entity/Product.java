package com.openstock.dev.searchservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openstock.dev.searchservice.entity.fields.Producer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Document(indexName = "products")
public class Product implements Serializable {

    @Id
    @Field(type = FieldType.Keyword, name = "productID")
    private String productID;
    @Field(type = FieldType.Keyword, name = "master")
    private String master;
    @Field(type = FieldType.Text, name = "country")
    private String country;
    @Field(type = FieldType.Text, name = "countryFlag", index = false)
    private String countryFlag;
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
    @Field(type = FieldType.Object, name = "prod")
    private Producer prod;
    @Field(type = FieldType.Text, name = "name")
    private String name;
    @Field(type = FieldType.Text, name = "variety")
    private String variety;
    @Field(type = FieldType.Text, name = "alc")
    private String alc;
    @Field(type = FieldType.Text, name = "vintage")
    private String vintage;
    @Field(type = FieldType.Text, name = "info", index = false)
    private String info;
    @Field(type = FieldType.Text, name = "img", index = false)
    private String img;
    @Field(type = FieldType.Boolean, name = "isBoosted")
    private Boolean isBoosted;
    @Field(type = FieldType.Integer, name = "boostPriority")
    private Integer boostPriority;
}


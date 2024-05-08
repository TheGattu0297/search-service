package com.openstock.dev.searchservice.kafka.messagemodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMessageModel {
    private String productID;
    private String country;
    private String type;
    private String subType;
    private String reg;
    private String sub;
    private String deno;
    private String prod;
    private String name;
    private String variety;
    private String alc;
    private String vintage;
    private String info;
    private String img;
}

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

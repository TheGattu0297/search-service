package com.openstock.dev.searchservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class ProductBoostRequestDTO {

    private String productID;
    private Integer boostPriority;  // Priority of boosting (1, 2, 3:Default)
    private Float ttlHours;
}

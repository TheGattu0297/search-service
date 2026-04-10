package com.openstock.dev.searchservice.kafka.messagemodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProducerModel {

    private String prodId;
    private String prodValue;
}


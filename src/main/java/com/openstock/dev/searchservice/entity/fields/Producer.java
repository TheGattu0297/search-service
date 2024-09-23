package com.openstock.dev.searchservice.entity.fields;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Producer implements Serializable {

    @Field(type = FieldType.Keyword, name = "prodId", index = false)
    private String prodId;

    @Field(type = FieldType.Text, name = "prodValue")
    private String prodValue;
}

package com.openstock.dev.searchservice.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HelperType {
    COUNTRY("Country", "country"),
    TYPE("Type", "type"),
    SUB_TYPE("Sub Type", "subType"),
    REGION("Region", "reg"),
    SUB_REGION("Sub Region", "sub"),
    DENOMINATION("Denomination", "deno"),
    PRODUCER("Producer", "prod"),
    NAME("Name", "name"),
    VARIETY("Variety", "variety"),
    ALCOHOL("Alcohol", "alc"),
    VINTAGE("Vintage", "vintage"),
    INFO("Info", "info"),
    IMAGE("Image", "img");

    private final String displayName; // Capitalized for display
    private final String fieldName;   // Lowercase for use in ProductMessageModel

    // Static method to retrieve HelperType by field name
    public static HelperType fromFieldName(String fieldName) {
        for (HelperType type : values()) {
            if (type.getFieldName().equalsIgnoreCase(fieldName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant for fieldName: " + fieldName);
    }
}

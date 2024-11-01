package com.openstock.dev.searchservice.constants;

import java.time.format.DateTimeFormatter;

public final class Constants {

    public static final String ELASTIC_INDEX = "products";

    public static final String DB_CALL = "DB CALL";

    public static final String NOT_FOUND = " not found.";

    public static final String PRODUCT_WITH_ID = "Product with ID ";

    public static final String BOOST_PARAMETER = "isBoosted";

    public static final String BOOST_PRIORITY = "boostPriority";

    // Prefix for cache group
    public static final String PRODUCT_CACHE_PREFIX = "SS:";

    // Kafka Topics & Constants
    public static final String KAFKA_GROUP_ID = "OS-Product";

    public static final String MASTER_INSERT_TOPIC = "OS_PRODUCT_TOPIC";

    public static final String HELPER_UPDATE_TOPIC = "OS_PRODUCT_UPDATE_TOPIC";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Constants() {
    }
}

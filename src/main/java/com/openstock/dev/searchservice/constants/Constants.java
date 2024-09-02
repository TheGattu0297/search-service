package com.openstock.dev.searchservice.constants;

import java.time.format.DateTimeFormatter;

public final class Constants {

    public static final String ELASTIC_INDEX = "products";

    public static final String DB_CALL = "DB CALL";

    public static final String ELASTIC_AUTOCOMPLETE = "autocomplete";

    public static final String ELASTIC_AUTO = "AUTO";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Constants() {
    }

}

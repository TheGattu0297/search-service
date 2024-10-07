package com.openstock.dev.searchservice.exceptions;

import static com.openstock.dev.searchservice.constants.Constants.NOT_FOUND;
import static com.openstock.dev.searchservice.constants.Constants.PRODUCT_WITH_ID;

// Custom exception classes
public class SearchServiceExceptions {

    // Private constructor to prevent instantiation
    private SearchServiceExceptions() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // Exception when the product is not found
    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String productId) {
            super(PRODUCT_WITH_ID + productId + NOT_FOUND);
        }
    }

    // Exception when the product is already boosted
    public static class ProductAlreadyBoostedException extends RuntimeException {
        public ProductAlreadyBoostedException(String productId) {
            super(PRODUCT_WITH_ID + productId + " is already boosted. Please remove the existing boost before adding a new one.");
        }
    }

    // Exception when the product is not boosted but a removal is attempted
    public static class ProductNotBoostedException extends RuntimeException {
        public ProductNotBoostedException(String productId) {
            super(PRODUCT_WITH_ID + productId + " is not boosted. Cannot remove boost.");
        }
    }
}

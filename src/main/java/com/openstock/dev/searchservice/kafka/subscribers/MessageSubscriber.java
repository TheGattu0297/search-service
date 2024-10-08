package com.openstock.dev.searchservice.kafka.subscribers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstock.dev.searchservice.constants.HelperType;
import com.openstock.dev.searchservice.entity.Product;
import com.openstock.dev.searchservice.entity.fields.Producer;
import com.openstock.dev.searchservice.kafka.messagemodel.ProductMessageModel;
import com.openstock.dev.searchservice.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.openstock.dev.searchservice.constants.Constants.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class MessageSubscriber {

    private final DataService dataService;

    @KafkaListener(topics = MASTER_INSERT_TOPIC, groupId = KAFKA_GROUP_ID, containerFactory = "kafkaListenerFactory")
    public void consumeProductInsertMessage(List<LinkedHashMap<String, Object>> messageInfoDTOList) {
        log.info("Message received for insertion into Product(s): {}", messageInfoDTOList);
        ObjectMapper mapper = new ObjectMapper();

        // Convert each LinkedHashMap to object
        List<Product> toSave = messageInfoDTOList.stream()
                .map(map -> mapper.convertValue(map, ProductMessageModel.class)) // Convert LinkedHashMap to ProductMessageModel
                .filter(Objects::nonNull) // Filter out any null values if conversion failed
                .map(data -> {

                    Producer producer = (data.getProd() != null) ?
                            new Producer(data.getProd().getProdId(), data.getProd().getProdValue()) : null;

                    return Product.builder()
                            .productID(data.getProductID())
                            .master(data.getMaster())
                            .country(data.getCountry())
                            .countryFlag(data.getCountryFlag())
                            .type(data.getType())
                            .subType(data.getSubType())
                            .reg(data.getReg())
                            .sub(data.getSub())
                            .deno(data.getDeno())
                            .prod(producer)
                            .name(data.getName())
                            .variety(data.getVariety())
                            .alc(data.getAlc())
                            .vintage(data.getVintage())
                            .info(data.getInfo())
                            .img(data.getImg())
                            .isBoosted(Boolean.FALSE)
                            .boostPriority(null)
                            .build();
                })
                .toList(); // Collect to a List of Product entities

        dataService.saveProducts(toSave); // Assuming saveProducts can handle a list of products
        log.info("Processed and saved products: {}", toSave.size());
    }

    @KafkaListener(topics = HELPER_UPDATE_TOPIC, groupId = KAFKA_GROUP_ID, containerFactory = "kafkaListenerFactory")
    public void consumeHelperUpdateMessage(Map<String, Object> payload) {
        log.info("Received helper update message: {}", payload);

        try {
            // Retrieve helperType from the payload
            String helperTypeString = (String) payload.get("helperType");
            if (helperTypeString == null) {
                log.error("Missing 'helperType' in the payload.");
                return; // Early exit if helperType is missing
            }

            // Convert helperType string to enum
            HelperType helperType = HelperType.fromFieldName(helperTypeString);

            // Safely extract product IDs from the payload
            Object productIdsObj = payload.get("productIds");
            Set<String> productIds;
            if (productIdsObj instanceof List<?>) {
                try {
                    productIds = ((List<?>) productIdsObj).stream()
                            .map(Object::toString)  // Safely cast each object to String
                            .collect(Collectors.toSet());
                } catch (ClassCastException e) {
                    log.error("Invalid type in productIds list: {}", e.getMessage());
                    return; // Early exit if product IDs are invalid
                }
            } else {
                log.error("Invalid or missing 'productIds' field in the payload.");
                return; // Early exit if product IDs are missing
            }

            if (productIds.isEmpty()) {
                log.warn("No Product IDs found in the payload.");
                return; // Early exit if product IDs are empty
            }

            // Extract the updated value (e.g., country, type, etc.) based on the helper type
            String updatedValue = (String) payload.get(helperType.getFieldName());
            // Validate that updated value is present
            if (updatedValue == null) {
                log.warn("No updated value found for helperType: {}", helperType.getDisplayName());
            }

            // Extract the updated flag (only for Country)
            String updatedFlag = null;
            if (helperType == HelperType.COUNTRY) {
                updatedFlag = (String) payload.get("countryFlag");
            }



            // Proceed with updating products using the extracted values
            dataService.updateProducts(helperType, productIds, updatedValue, updatedFlag);

            log.info("Successfully processed helper update for helperType: {} with {} products updated.",
                    helperType.getDisplayName(), productIds.size());

        } catch (Exception e) {
            log.error("Error processing helper update message: {}", e.getMessage(), e);
        }
    }
}

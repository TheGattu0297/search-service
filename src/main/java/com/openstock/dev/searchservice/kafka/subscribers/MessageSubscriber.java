package com.openstock.dev.searchservice.kafka.subscribers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstock.dev.searchservice.constants.HelperType;
import com.openstock.dev.searchservice.entity.Product;
import com.openstock.dev.searchservice.kafka.messagemodel.ProductMessageModel;
import com.openstock.dev.searchservice.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;

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
                .map(data -> Product.builder()
                        .productID(data.getProductID())
                        .master(data.getMaster())
                        .country(data.getCountry())
                        .countryFlag(data.getCountryFlag())
                        .type(data.getType())
                        .subType(data.getSubType())
                        .reg(data.getReg())
                        .sub(data.getSub())
                        .deno(data.getDeno())
                        .prod(data.getProd())
                        .name(data.getName())
                        .variety(data.getVariety())
                        .alc(data.getAlc())
                        .vintage(data.getVintage())
                        .info(data.getInfo())
                        .img(data.getImg())
                        .build())
                .toList(); // Collect to a List of Product entities

        dataService.saveProducts(toSave); // Assuming saveProducts can handle a list of products
        log.info("Processed and saved products: {}", toSave.size());
    }

    @KafkaListener(topics = HELPER_UPDATE_TOPIC, groupId = KAFKA_GROUP_ID, containerFactory = "kafkaListenerFactory")
    public void consumeHelperUpdateMessage(LinkedHashMap<String, Object> payload) {
        log.info("Received helper update message: {}", payload);

        try {
            // Extract helperType, productIds, updatedValue, and optionally updatedFlag
            HelperType helperType = HelperType.fromFieldName(payload.get("helperType").toString());
            Set<String> productIds = new HashSet<>((List<String>) payload.get("productIds"));
            String updatedValue = (String) payload.get(helperType.getFieldName());
            String updatedFlag = (String) payload.get("countryFlag"); // Only applicable for country

            if (productIds.isEmpty() || updatedValue == null) {
                log.warn("Invalid message: missing product IDs or updated value");
                return;
            }

            // Delegate the update logic to the product service
            dataService.updateProducts(helperType, productIds, updatedValue, updatedFlag);

            log.info("Successfully processed update for helperType: {} and updated {} products.",
                    helperType.getDisplayName(), productIds.size());

        } catch (Exception e) {
            log.error("Error processing helper update message: {}", e.getMessage(), e);
        }
    }


}

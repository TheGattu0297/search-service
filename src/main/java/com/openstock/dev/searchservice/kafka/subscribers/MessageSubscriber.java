package com.openstock.dev.searchservice.kafka.subscribers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstock.dev.searchservice.kafka.messagemodel.ProductMessageModel;
import com.openstock.dev.searchservice.entity.Product;
import com.openstock.dev.searchservice.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MessageSubscriber {
    private final ElasticSearchService elasticSearchService;

    public MessageSubscriber(ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }


    @KafkaListener(topics = "OS_PRODUCT_TOPIC", groupId = "OS-Product", containerFactory = "kafkaListenerFactory")
    public void consumeMessage(List<LinkedHashMap<String, Object>> messageInfoDTOList) {
        log.info("Message received for update saving: {}", messageInfoDTOList);
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

        elasticSearchService.saveProducts(toSave); // Assuming saveProducts can handle a list of products
        log.info("Processed and saved products: {}", toSave.size());
    }

}

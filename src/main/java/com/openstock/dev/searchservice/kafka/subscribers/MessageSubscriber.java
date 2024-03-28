package com.openstock.dev.searchservice.kafka.subscribers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openstock.dev.searchservice.kafka.messagemodel.ProductMessageModel;
import com.openstock.dev.searchservice.model.Product;
import com.openstock.dev.searchservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageSubscriber {
   private final  ProductService productService;

    public MessageSubscriber(ProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = "OS_PRODUCT_TOPIC", groupId = "OS-Product", containerFactory = "kafkaListenerFactory")
    public void consumeMessage(List<LinkedHashMap<String, Object>> messageInfoDTOList) {
        log.info("Message received for update saving:##### " + messageInfoDTOList);
        ObjectMapper mapper = new ObjectMapper();

        // Convert each LinkedHashMap to Person object
        List<Product> toSave = messageInfoDTOList.stream()
                .map(map -> mapper.convertValue(map, ProductMessageModel.class)) // Convert LinkedHashMap to ProductMessageModel
                .filter(Objects::nonNull) // Filter out any null values if conversion failed
                .map(data -> Product.builder()
                        .productID(data.getProductID())
                        .country(data.getCountry())
                        .type(data.getType())
                        .region(data.getRegion())
                        .subRegion(data.getSubRegion())
                        .denomination(data.getDenomination())
                        .producer(data.getProducer())
                        .name(data.getName())
                        .variety(data.getVariety())
                        .alcoholPercentage(data.getAlcoholPercentage())
                        .vintage(data.getVintage())
                        .info(data.getInfo())
                        .build())
                .collect(Collectors.toList()); // Collect to a List of Product entities

        productService.saveProducts(toSave); // Assuming saveProducts can handle a list of products
        log.info("Processed and saved products: " + toSave.size());
    }

}

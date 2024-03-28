package com.openstock.dev.searchservice.kafka.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfiguration {
    @Value("${kafka.url}")
    String url;
    @Value("${kafka.port}")
    String port;

    @Bean
    public ConsumerFactory<String, Object> consumerFactory(@Value("${kafka.group.id:OS-Product}") String groupId,
                                                           @Value("${kafka.requestTimeoutMS:500000}") String requestTimeoutMS,
                                                           @Value("${kafka.heartbeatIntervalMS:1000}") String heartbeatIntervalMS,
                                                           @Value("${kafka.maxPollIntervalMS:900000}") String maxPollIntervalMS,
                                                           @Value("${kafka.maxPollRecordsMs:100}") String maxPollRecordsMs,
                                                           @Value("${kafka.sessionTimeoutMs:600000}") String sessionTimeoutMs) {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, url + ":" + port);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMS);
        config.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatIntervalMS);
        config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMS);
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecordsMs);
        config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        JsonDeserializer<Object> deserializer = new JsonDeserializer<>(Object.class);
        deserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerFactory(@Nullable DeadLetterPublishingRecoverer recoverer, ConsumerFactory consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}

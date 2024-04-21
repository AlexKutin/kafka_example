package com.example.kafkaproducer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    private static final int REPLICA_COUNT = 2;
    private static final int BATCH_SIZE = 5;

    @Value("${kafka.topics.topic-name-1}")
    private String topicName1;

    @Value("${kafka.topics.topic-name-2}")
    private String topicName2;

    @Value("${kafka.topics.topic-partitions-2}")
    private int countPartitions2;

    @Value("${spring.kafka.bootstrap-servers}")
    private String addresses;

    @Bean(name = "topicWithoutPartitions")
    public NewTopic topic1() {
        return TopicBuilder
                .name(topicName1)
                .replicas(REPLICA_COUNT)
                .build();
    }

    @Bean(name = "topicWithPartitions")
    public NewTopic topic2() {
        return TopicBuilder
                .name(topicName2)
                .partitions(countPartitions2)
                .replicas(REPLICA_COUNT)
                .build();
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, addresses);
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, BATCH_SIZE);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }
}

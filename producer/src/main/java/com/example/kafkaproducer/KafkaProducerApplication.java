package com.example.kafkaproducer;

import com.example.kafkaproducer.service.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class KafkaProducerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(KafkaProducerApplication.class, args);
        KafkaProducer producer = context.getBean(KafkaProducer.class);
        try {
            producer.createTopicsAndSendMessages();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

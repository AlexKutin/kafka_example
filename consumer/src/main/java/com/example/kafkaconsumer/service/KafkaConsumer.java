package com.example.kafkaconsumer.service;

import com.example.kafkaconsumer.domain.SimpleMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final ObjectMapper objectMapper;

    public KafkaConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = {"${kafka.topics.topic-name-1}", "${kafka.topics.topic-name-2}"},
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(@Header(KafkaHeaders.OFFSET) long offset,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                       @Payload String msg) throws JsonProcessingException {
        SimpleMessage simpleMessage = objectMapper.readValue(msg, SimpleMessage.class);
        logger.info("Received message: {}, partition = {}, offset = {}", simpleMessage, partition, offset);
    }

}

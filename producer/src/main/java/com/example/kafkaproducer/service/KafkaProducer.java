package com.example.kafkaproducer.service;

import com.example.kafkaproducer.domain.SimpleMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
public class KafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private static final int COUNT_MESSAGES = 10;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final KafkaAdmin kafkaAdmin;
    private final NewTopic topic1;
    private final NewTopic topic2;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, KafkaAdmin kafkaAdmin,
                         @Qualifier("topicWithoutPartitions") NewTopic topic1,
                         @Qualifier("topicWithPartitions") NewTopic topic2) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.kafkaAdmin = kafkaAdmin;
        this.topic1 = topic1;
        this.topic2 = topic2;
    }

    public void createTopicsAndSendMessages() throws JsonProcessingException {
        sendMessagesToTopic(topic1);
        sendMessagesToTopic(topic2);
    }

    public void sendMessagesToTopic(NewTopic topic) throws JsonProcessingException {
        kafkaAdmin.createOrModifyTopics(topic);

        logger.info("Send messages to topic: " + topic);
        int messageNumber = 1;
        while (messageNumber <= COUNT_MESSAGES) {
            SimpleMessage message = new SimpleMessage()
                    .setNumber(messageNumber)
                    .setMessage("message N: " + messageNumber);
            String msg = objectMapper.writeValueAsString(message);

            CompletableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(topic.name(), String.valueOf(ThreadLocalRandom.current().nextLong()), msg);
            messageNumber++;

            future.whenComplete((result, throwable) -> {
                if (throwable == null) {
                    logger.info("Sent message=[{}] with offset=[{}] in partition=[{}]",
                            message, result.getRecordMetadata().offset(), result.getRecordMetadata().partition());
                } else {
                    logger.warn("Unable to send message=[{}] due to : {}", message, throwable.getMessage());
                }
            });
        }
    }

}

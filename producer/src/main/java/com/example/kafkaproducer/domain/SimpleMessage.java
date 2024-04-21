package com.example.kafkaproducer.domain;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SimpleMessage {
    private long number;
    private String message;
}

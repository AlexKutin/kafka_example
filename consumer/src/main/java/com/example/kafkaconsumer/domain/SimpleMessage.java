package com.example.kafkaconsumer.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SimpleMessage {
    private long number;
    private String message;
}

package com.qwertgold.spring.aws.messaging.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    String clientId;
    Object payload;
    Destination destination;
    Map<String, Header> headers;
}

package com.hellopublic.spring.aws.messaging.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    /**
     * A client identifier, which is stored if the message is persisted. This allows you to find specific messages and link then to entities in the
     * business domain
     */
    String clientId;
    Object payload;
    Map<String, Header> headers;
}

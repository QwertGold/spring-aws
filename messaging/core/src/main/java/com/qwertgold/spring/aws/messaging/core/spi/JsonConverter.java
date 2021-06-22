package com.qwertgold.spring.aws.messaging.core.spi;

import com.qwertgold.spring.aws.messaging.core.domain.Header;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.util.Map;

public interface JsonConverter {
    String toJson(Object x);
    <T> T fromJson(String json, Class<T> targetType);

    <T> T fromJsonByClassName(String json, String targetType);
    Map<String, Header> readHeaders(String headers);
}

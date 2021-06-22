package com.qwertgold.spring.aws.messaging.core.defaults;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.qwertgold.spring.aws.messaging.core.domain.Header;
import com.qwertgold.spring.aws.messaging.core.spi.JsonConverter;

import java.io.IOException;
import java.util.Map;

public class DefaultJsonMapper implements JsonConverter {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            ;

    private final MapType headerMapType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Header.class);

    public String toJson(Object x) {
        try {
            return objectMapper.writeValueAsString(x);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(String json, Class<T> targetType) {
        try {
            return objectMapper.readValue(json, targetType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJsonByClassName(String json, String targetType) {
        try {
            return objectMapper.readValue(json, (Class<T>) JsonConverter.class.getClassLoader().loadClass(targetType));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Header> readHeaders(String headers) {
        try {
            return objectMapper.readValue(headers, headerMapType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

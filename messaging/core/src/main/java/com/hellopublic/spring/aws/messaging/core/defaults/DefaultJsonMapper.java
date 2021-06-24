package com.hellopublic.spring.aws.messaging.core.defaults;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.hellopublic.spring.aws.messaging.core.customization.JsonConverter;
import com.hellopublic.spring.aws.messaging.core.domain.Header;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

public class DefaultJsonMapper implements JsonConverter {

    private final ObjectMapper objectMapper = createObjectMapper();
    private final MapType headerMapType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Header.class);

    @NotNull
    private ObjectMapper createObjectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(com.fasterxml.jackson.core.JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
    }

    public String toJson(Object x) {
        try {
            return objectMapper.writeValueAsString(x);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T fromJsonByClassName(String json, String targetType) {
        try {
            return objectMapper.readValue(json, (Class<T>) Class.forName(targetType));
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

package com.qwertgold.spring.aws.messaging.core.defaults;

import com.qwertgold.spring.aws.messaging.core.domain.Header;
import com.qwertgold.spring.aws.messaging.core.spi.HeaderExtractor;

import java.util.Map;

public class DefaultHeaderExtractor implements HeaderExtractor {

    public static final String classHeader = "class";
    public static final String typeHeader = "type";

    @Override
    public Map<String, Header> extractHeaders(Object payload) {
        return Map.of(
                classHeader, new Header("String", payload.getClass().getName()),
                typeHeader, new Header("String", payload.getClass().getSimpleName())
        );
    }
}

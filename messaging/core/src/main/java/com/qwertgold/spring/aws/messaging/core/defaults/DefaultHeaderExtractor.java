package com.qwertgold.spring.aws.messaging.core.defaults;

import com.qwertgold.spring.aws.messaging.core.domain.Header;
import com.qwertgold.spring.aws.messaging.core.spi.HeaderExtractor;

import java.util.Map;

public class DefaultHeaderExtractor implements HeaderExtractor {

    public static final String CLASS_HEADER_NAME = "class";
    public static final String EVENT_HEADER_NAME = "event";

    @Override
    public Map<String, Header> extractHeaders(Object payload) {
        return Map.of(
                CLASS_HEADER_NAME, new Header(Header.STRING_TYPE, payload.getClass().getName()),
                EVENT_HEADER_NAME, new Header(Header.STRING_TYPE, payload.getClass().getSimpleName())
        );
    }
}

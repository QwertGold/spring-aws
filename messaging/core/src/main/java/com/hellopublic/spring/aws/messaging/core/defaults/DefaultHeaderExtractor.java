package com.hellopublic.spring.aws.messaging.core.defaults;

import com.hellopublic.spring.aws.messaging.core.customization.HeaderExtractor;
import com.hellopublic.spring.aws.messaging.core.domain.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * Default header extractor, can be extended to extract additional headers
 */
public class DefaultHeaderExtractor implements HeaderExtractor {

    public static final String CLASS_HEADER_NAME = "class";
    public static final String EVENT_HEADER_NAME = "event";

    @Override
    public Map<String, Header> extractHeaders(Object payload) {

        Map<String, Header> headers = new HashMap<>();
        additionalHeaders(headers, payload);
        headers.put(CLASS_HEADER_NAME, new Header(Header.STRING_TYPE, payload.getClass().getName()));
        headers.put(EVENT_HEADER_NAME, new Header(Header.STRING_TYPE, payload.getClass().getSimpleName()));
        return Map.copyOf(headers);
    }

    /**
     * Override to add additional headers
     * @param headers a Map to add the additional headers to
     * @param payload the payload
     */
    protected void additionalHeaders(Map<String, Header> headers, Object payload) {

    }
}

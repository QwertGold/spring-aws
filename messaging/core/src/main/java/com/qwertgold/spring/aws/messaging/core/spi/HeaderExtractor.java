package com.qwertgold.spring.aws.messaging.core.spi;

import com.qwertgold.spring.aws.messaging.core.domain.Header;

import java.util.Map;

public interface HeaderExtractor {
    Map<String, Header> extractHeaders(Object payload);
}

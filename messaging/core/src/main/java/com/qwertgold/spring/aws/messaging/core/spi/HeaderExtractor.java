package com.qwertgold.spring.aws.messaging.core.spi;

import com.qwertgold.spring.aws.messaging.core.domain.Header;

import java.util.Map;

/**
 * Abstraction for extracting headers from a payload.
 * This is useful when connecting SQS to SNS because the subscription allows you to filter based on headers, if a queue is only interested in specific messages
 */
public interface HeaderExtractor {
    Map<String, Header> extractHeaders(Object payload);
}

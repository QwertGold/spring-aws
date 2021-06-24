package com.hellopublic.spring.aws.messaging.core.customization;

import com.hellopublic.spring.aws.messaging.core.domain.Header;

import java.util.Map;

/**
 * Abstraction for converting objects to Json or back
 */
public interface JsonConverter {
    String toJson(Object x);
    <T> T fromJsonByClassName(String json, String targetType);
    Map<String, Header> readHeaders(String headers);
}

package com.qwertgold.spring.aws.messaging.core.domain;

import lombok.Value;

/**
 * A Header is meta data which is sent along with a payload. The default is to encode the fully qualified class name, so the receiver of the message
 * knows how to deserialize it.
 */
@Value
public class Header {
    public static final String STRING_TYPE = "String";
    String type;
    String value;
}

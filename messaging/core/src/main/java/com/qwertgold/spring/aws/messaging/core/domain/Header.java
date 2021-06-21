package com.qwertgold.spring.aws.messaging.core.domain;

import lombok.Value;

/**
 * A Header is meta data which is send along with a payload. In AWS headers are unencrypted and can be inspected in filters
 */
@Value
public class Header {
    String type;
    String value;
}

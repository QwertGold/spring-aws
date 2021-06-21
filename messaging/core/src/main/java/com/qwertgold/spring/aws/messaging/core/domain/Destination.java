package com.qwertgold.spring.aws.messaging.core.domain;

import lombok.Value;

/**
 * a destination identified by a name and a type, where messages can be routed
 */
@Value
public class Destination {
    String destination;
    String destinationType;
}

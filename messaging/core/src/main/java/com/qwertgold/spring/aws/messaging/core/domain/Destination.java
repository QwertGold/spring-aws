package com.qwertgold.spring.aws.messaging.core.domain;

import lombok.Value;

/**
 * a destination identified by a name and a type, where messages can be routed. Each MessageSink has be able to translate the destination when creating Api
 * specific request, like queueURL for SQS and topicARN for SNS
 *
 */
@Value
public class Destination {
    String destination;
    String destinationType;
}

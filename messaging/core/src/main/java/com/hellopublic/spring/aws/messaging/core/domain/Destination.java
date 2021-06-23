package com.hellopublic.spring.aws.messaging.core.domain;

import lombok.Value;

/**
 * a destination identified by a name and a type, where messages can be routed. A Destination has two properties a type, which the framework uses to find
 * the appropriate MessageRouter, and a destination name, which the MessageRouter uses to translate the routed messages into Api specific request for
 * the destination, like the queueURL for AWS SQS or a topicArn for SNS
 */
@Value
public class Destination {
    String target;
    String type;
}

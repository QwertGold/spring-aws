package com.hellopublic.spring.aws.messaging.core.domain;

import lombok.Value;

/**
 * a destination identified by a name and a type, where messages can be routed. The framework uses the destination-type to find the MessageRouter
 * which can route a message to the destination-target. The MessageRouter will be responsible for translating the destination-target to an API specific
 * destination, such as a QueueURL or TopicARN
 */
@Value
public class Destination {
    String target;
    String type;
}

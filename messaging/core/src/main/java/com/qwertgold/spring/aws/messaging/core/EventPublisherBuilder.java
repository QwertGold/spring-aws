package com.qwertgold.spring.aws.messaging.core;

import com.google.common.base.Preconditions;
import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
public class EventPublisherBuilder {
    private final EventPublisherFactory eventPublisherFactory;

    private Destination destination;

    public EventPublisherBuilder(EventPublisherFactory eventPublisherFactory) {
        this.eventPublisherFactory = eventPublisherFactory;
    }

    public EventPublisher build() {
        Preconditions.checkNotNull(destination, "Destination must be configured");
        return eventPublisherFactory.build(this);
    }

    public EventPublisherBuilder withDestination(Destination destination) {
        this.destination = destination;
        return this;
    }
}

package com.qwertgold.spring.aws.messaging.persistence;

import com.google.common.base.Preconditions;
import com.qwertgold.spring.aws.messaging.core.EventPublisher;
import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor
public class PersistentEventPublisherBuilder {

    private final PersistenceEventPublisherFactory messagingFactory;
    private Destination destination;

    public EventPublisher build() {
        Preconditions.checkNotNull(destination, "Destination must be configured");
        return messagingFactory.build(this);
    }

    public PersistentEventPublisherBuilder withDestination(Destination destination) {
        this.destination = destination;
        return this;
    }


}

package com.qwertgold.spring.aws.messaging.test;

import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSink;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSinkFactory;
import lombok.Getter;

import java.util.Set;

public class TestMessageSinkFactory implements MessageSinkFactory {

    public static final String MOCK_DESTINATION = "MOCK";
    @Getter
    private final TestMessageSink messageSink = new TestMessageSink();

    @Override
    public Set<String> supportedDestinations() {
        return Set.of(MOCK_DESTINATION);
    }

    @Override
    public MessageSink createSink(Destination destination) {
        return messageSink;
    }
}

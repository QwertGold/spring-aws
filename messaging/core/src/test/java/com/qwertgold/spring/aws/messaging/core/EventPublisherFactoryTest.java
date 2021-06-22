package com.qwertgold.spring.aws.messaging.core;

import com.qwertgold.spring.aws.messaging.core.defaults.DefaultHeaderExtractor;
import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSinkFactory;
import com.qwertgold.spring.aws.messaging.test.TestMessageSinkFactory;
import org.junit.Test;

import java.util.List;

import static com.qwertgold.spring.aws.messaging.core.MessageSinkFactoryManager.UNSUPPORTED_DESTINATION_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EventPublisherFactoryTest {

    @Test
    public void given_destination_publisher_is_created() {
        EventPublisherFactory factory = createValidFactory();
        EventPublisher publisher = factory.createPublisher(new Destination("dummy", TestMessageSinkFactory.MOCK_DESTINATION_TYPE));
        assertThat(publisher).isNotNull();
    }


    @Test
    public void given_unknown_destination_type_exception_is_thrown() {
        EventPublisherFactory factory = createValidFactory();
        assertThatThrownBy(()-> factory.createPublisher(new Destination("dummy", "unknown-destination-type")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format(UNSUPPORTED_DESTINATION_TYPE, "unknown-destination-type"));
    }


    private EventPublisherFactory createValidFactory() {
        TestMessageSinkFactory sink = new TestMessageSinkFactory();
        List<MessageSinkFactory> factoryList = List.of(sink);
        MessageSinkFactoryManager manager = new MessageSinkFactoryManager(factoryList);
        manager.createFactoryMap();
        return new EventPublisherFactory(manager, new DefaultHeaderExtractor());
    }

}

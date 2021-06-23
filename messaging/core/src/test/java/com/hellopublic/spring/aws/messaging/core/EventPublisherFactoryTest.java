package com.hellopublic.spring.aws.messaging.core;

import com.hellopublic.spring.aws.messaging.core.defaults.DefaultHeaderExtractor;
import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouterFactory;
import com.hellopublic.spring.aws.messaging.test.TestMessageRouterFactory;
import org.junit.Test;

import java.util.List;

import static com.hellopublic.spring.aws.messaging.core.MessageRouterFactoryManager.UNSUPPORTED_DESTINATION_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EventPublisherFactoryTest {

    @Test
    public void given_destination_publisher_is_created() {
        EventPublisherFactory factory = createValidFactory();
        EventPublisher publisher = factory.createPublisher(new Destination("dummy", TestMessageRouterFactory.MOCK_DESTINATION_TYPE));
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
        TestMessageRouterFactory routerFactory = new TestMessageRouterFactory();
        List<MessageRouterFactory> factoryList = List.of(routerFactory);
        MessageRouterFactoryManager manager = new MessageRouterFactoryManager(factoryList);
        manager.createFactoryMap();
        return new EventPublisherFactoryImpl(manager, new DefaultHeaderExtractor());
    }

}

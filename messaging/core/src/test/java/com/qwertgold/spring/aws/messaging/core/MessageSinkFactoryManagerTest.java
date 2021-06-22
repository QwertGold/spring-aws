package com.qwertgold.spring.aws.messaging.core;


import com.qwertgold.spring.aws.messaging.core.spi.MessageSinkFactory;
import com.qwertgold.spring.aws.messaging.test.TestMessageSinkFactory;
import org.junit.Test;

import java.util.List;

import static com.qwertgold.spring.aws.messaging.test.TestMessageSinkFactory.MOCK_DESTINATION_TYPE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MessageSinkFactoryManagerTest {

    @Test
    public void given_empty_list_of_sinks_factory_will_throw_exception_on_post_construct() {
        MessageSinkFactoryManager manager = new MessageSinkFactoryManager(List.of());
        assertThatThrownBy(()-> manager.createFactoryMap()).isInstanceOf(IllegalStateException.class).hasMessage(MessageSinkFactoryManager.NO_SINKS);
    }

    @Test
    public void given_multiple_formats_for_destination_exception_is_thrown() {
        List<MessageSinkFactory> factoryList = List.of(new TestMessageSinkFactory(), new TestMessageSinkFactory());
        MessageSinkFactoryManager manager = new MessageSinkFactoryManager(factoryList);
        assertThatThrownBy(()-> manager.createFactoryMap()).isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format(MessageSinkFactoryManager.MULTIPLE_FACTORIES_FORMAT, MOCK_DESTINATION_TYPE, factoryList));


    }

}

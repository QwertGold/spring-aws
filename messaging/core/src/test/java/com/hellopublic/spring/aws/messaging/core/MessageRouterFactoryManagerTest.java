package com.hellopublic.spring.aws.messaging.core;


import com.hellopublic.spring.aws.messaging.core.spi.MessageRouterFactory;
import com.hellopublic.spring.aws.messaging.test.TestMessageRouterFactory;
import org.junit.Test;

import java.util.List;

import static com.hellopublic.spring.aws.messaging.test.TestMessageRouterFactory.MOCK_DESTINATION_TYPE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MessageRouterFactoryManagerTest {

    @Test
    public void given_empty_list_of_router_factory_will_throw_exception_on_post_construct() {
        MessageRouterFactoryManager manager = new MessageRouterFactoryManager(List.of());
        assertThatThrownBy(manager::createFactoryMap).isInstanceOf(IllegalStateException.class).hasMessage(MessageRouterFactoryManager.NO_ROUTERS);
    }

    @Test
    public void given_multiple_routers_for_same_destination_type_exception_is_thrown() {
        List<MessageRouterFactory> factoryList = List.of(new TestMessageRouterFactory(), new TestMessageRouterFactory());
        MessageRouterFactoryManager manager = new MessageRouterFactoryManager(factoryList);
        assertThatThrownBy(manager::createFactoryMap).isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format(MessageRouterFactoryManager.MULTIPLE_FACTORIES_FORMAT, MOCK_DESTINATION_TYPE, factoryList));


    }

}

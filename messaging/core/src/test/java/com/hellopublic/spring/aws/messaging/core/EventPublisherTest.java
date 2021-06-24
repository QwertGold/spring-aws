package com.hellopublic.spring.aws.messaging.core;

import com.hellopublic.spring.aws.messaging.Helper;
import com.hellopublic.spring.aws.messaging.TestPayloadDto;
import com.hellopublic.spring.aws.messaging.core.defaults.DefaultHeaderExtractor;
import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.domain.Header;
import com.hellopublic.spring.aws.messaging.core.domain.Message;
import com.hellopublic.spring.aws.messaging.core.util.IdGenerator;
import com.hellopublic.spring.aws.messaging.test.TestMessageRouter;
import com.hellopublic.spring.aws.messaging.test.TestMessageRouterFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class EventPublisherTest {

    @Test
    public void given_payload_is_published_header_are_extracted_and_message_delivered_to_router() {

        TestMessageRouter router = new TestMessageRouter();
        Destination destination = new Destination("dummy", TestMessageRouterFactory.MOCK_DESTINATION_TYPE);
        EventPublisher publisher = new EventPublisher(router, destination,new DefaultHeaderExtractor());
        TestPayloadDto payload = Helper.createPayload();
        publisher.send(payload);

        assertThat(router.getMessages()).hasSize(1);
        Message message = router.getMessages().get(0);
        assertThat(message.getClientId()).isNotBlank();
        assertThat(message.getHeaders())
                .hasSize(2)
                .contains(entry(DefaultHeaderExtractor.CLASS_HEADER_NAME, new Header(Header.STRING_TYPE, TestPayloadDto.class.getName())))
                .contains(entry(DefaultHeaderExtractor.EVENT_HEADER_NAME, new Header(Header.STRING_TYPE, TestPayloadDto.class.getSimpleName())));
        assertThat(message.getPayload()).isSameAs(payload);
    }

    @Test
    public void given_explicit_client_id_value_is_added_to_message() {
        TestMessageRouter router = new TestMessageRouter();
        Destination destination = new Destination("dummy", TestMessageRouterFactory.MOCK_DESTINATION_TYPE);
        EventPublisher publisher = new EventPublisher(router, destination,new DefaultHeaderExtractor());
        TestPayloadDto payload = Helper.createPayload();
        String clientId = IdGenerator.generateId();
        publisher.send(payload, clientId);

        assertThat(router.getMessages()).hasSize(1);
        Message message = router.getMessages().get(0);
        assertThat(message.getClientId()).isEqualTo(clientId);
    }

}

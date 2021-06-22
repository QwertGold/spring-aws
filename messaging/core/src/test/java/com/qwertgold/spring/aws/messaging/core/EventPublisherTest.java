package com.qwertgold.spring.aws.messaging.core;

import com.qwertgold.spring.aws.messaging.Helper;
import com.qwertgold.spring.aws.messaging.TestPayloadDto;
import com.qwertgold.spring.aws.messaging.core.EventPublisher;
import com.qwertgold.spring.aws.messaging.core.defaults.DefaultHeaderExtractor;
import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.domain.Header;
import com.qwertgold.spring.aws.messaging.core.domain.Message;
import com.qwertgold.spring.aws.messaging.core.util.IdGenerator;
import com.qwertgold.spring.aws.messaging.test.TestMessageSink;
import com.qwertgold.spring.aws.messaging.test.TestMessageSinkFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class EventPublisherTest {

    @Test
    public void given_payload_is_published_header_are_extracted_and_message_delivered_to_sink() {

        TestMessageSink sink = new TestMessageSink();
        Destination destination = new Destination("dummy", TestMessageSinkFactory.MOCK_DESTINATION_TYPE);
        EventPublisher publisher = new EventPublisher(sink, destination,new DefaultHeaderExtractor());
        TestPayloadDto payload = Helper.createPayload();
        publisher.send(payload);

        assertThat(sink.getMessages()).hasSize(1);
        Message message = sink.getMessages().get(0);
        assertThat(message.getClientId()).isNotBlank();
        assertThat(message.getDestination()).isEqualTo(destination);
        assertThat(message.getHeaders())
                .hasSize(2)
                .contains(entry(DefaultHeaderExtractor.CLASS_HEADER_NAME, new Header(Header.STRING_TYPE, TestPayloadDto.class.getName())))
                .contains(entry(DefaultHeaderExtractor.EVENT_HEADER_NAME, new Header(Header.STRING_TYPE, TestPayloadDto.class.getSimpleName())));
        assertThat(message.getPayload()).isSameAs(payload);
    }

    @Test
    public void given_explicit_client_id_value_is_added_to_message() {
        TestMessageSink sink = new TestMessageSink();
        Destination destination = new Destination("dummy", TestMessageSinkFactory.MOCK_DESTINATION_TYPE);
        EventPublisher publisher = new EventPublisher(sink, destination,new DefaultHeaderExtractor());
        TestPayloadDto payload = Helper.createPayload();
        String clientId = IdGenerator.generateId();
        publisher.send(payload, clientId);

        assertThat(sink.getMessages()).hasSize(1);
        Message message = sink.getMessages().get(0);
        assertThat(message.getClientId()).isEqualTo(clientId);
    }

}

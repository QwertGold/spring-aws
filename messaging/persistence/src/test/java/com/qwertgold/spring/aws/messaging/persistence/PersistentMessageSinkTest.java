package com.qwertgold.spring.aws.messaging.persistence;


import com.qwertgold.spring.aws.messaging.Helper;
import com.qwertgold.spring.aws.messaging.TestPayloadDto;
import com.qwertgold.spring.aws.messaging.core.EventPublisher;
import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.persistence.dao.JdbcMessageRepository;
import com.qwertgold.spring.aws.messaging.persistence.dao.PersistedMessage;
import com.qwertgold.spring.aws.messaging.test.TestMessageSink;
import com.qwertgold.spring.aws.messaging.test.TestMessageSinkFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("postgres")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TestApplication.class)
public class PersistentMessageSinkTest extends PersistenceTestCase {

    @Test
    public void given_message_is_received_it_is_stored_and_forwarded_to_decorated_sink() {

        EventPublisher publisher = messageFactory.builder()
                .withDestination(new Destination("dummy", TestMessageSinkFactory.MOCK_DESTINATION))
                .build();

        TestPayloadDto payload = Helper.createPayload();
        publisher.send(payload);

        TestMessageSink testMessageSink = testMessageSinkFactory.getMessageSink();

        assertThat(testMessageSink.getMessages()).hasSize(1);

        List<PersistedMessage> unsentMessages = jdbcMessageRepository.findUnsentMessages(10);
        assertThat(unsentMessages).isEmpty();

        List<PersistedMessage> allMessages = jdbcMessageRepository.findAllMessages(10);
        assertThat(allMessages).hasSize(1);
        PersistedMessage persistedMessage = allMessages.get(0);
        assertThat(persistedMessage.getStatus()).isEqualTo(JdbcMessageRepository.SENT);
    }
}

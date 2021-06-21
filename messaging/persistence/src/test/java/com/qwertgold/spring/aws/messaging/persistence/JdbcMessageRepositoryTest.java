package com.qwertgold.spring.aws.messaging.persistence;

import com.qwertgold.spring.aws.messaging.Helper;
import com.qwertgold.spring.aws.messaging.core.domain.Message;
import com.qwertgold.spring.aws.messaging.persistence.dao.JdbcMessageRepository;
import com.qwertgold.spring.aws.messaging.persistence.dao.PersistedMessage;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public abstract class JdbcMessageRepositoryTest extends PersistenceTestCase {

    @Test
    public void test() {

        Message message = Helper.createMessage("MOCK");
        String id = jdbcMessageRepository.storeMessage(message);
        assertThat(id).isNotBlank();
        List<PersistedMessage> unsentMessages = jdbcMessageRepository.findUnsentMessages(10);
        assertThat(unsentMessages).hasSize(1);
        PersistedMessage persistedMessage = unsentMessages.get(0);

        assertThat(persistedMessage.getMessage()).isEqualTo(message);
        assertThat(persistedMessage.getStatus()).isEqualTo(JdbcMessageRepository.UNSENT);
        assertThat(persistedMessage.getNextSend()).isNotNull();
        assertThat(persistedMessage.getCreated()).isNotNull();

        jdbcMessageRepository.markAsSent(id);
        unsentMessages = jdbcMessageRepository.findUnsentMessages(10);
        assertThat(unsentMessages).hasSize(0);

    }

}

package com.hellopublic.spring.aws.messaging.persistence;

import com.hellopublic.spring.aws.messaging.Helper;
import com.hellopublic.spring.aws.messaging.core.domain.Message;
import com.hellopublic.spring.aws.messaging.persistence.dao.JdbcMessageRepository;
import com.hellopublic.spring.aws.messaging.persistence.dao.PersistedMessage;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public abstract class JdbcMessageRepositoryTest extends PersistenceTestCase {

    @Test
    public void given_a_message_is_stored_it_can_be_found_and_marked_as_sent() {

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

    @Test
    public void given_more_than_max_rows_are_stored_only_max_rows_are_returned() {

        for (int i = 0; i < 10; i++) {
            Message message = Helper.createMessage("MOCK").setClientId(String.valueOf(i));
            jdbcMessageRepository.storeMessage(message);
        }

        List<PersistedMessage> unsentMessages = jdbcMessageRepository.findUnsentMessages(5);
        assertThat(unsentMessages).hasSize(5);
        // The test makes an assumption here. For the order to be true, the created_at field must be unique.
        // As long as the database type and the OS uses microsecond resolution, this seems like a fair assumption
        List<String> clientIds = unsentMessages.stream().map(PersistedMessage::getMessage).map(Message::getClientId).collect(Collectors.toList());
        assertThat(clientIds).containsExactly("0", "1", "2", "3", "4");
        for (PersistedMessage unsentMessage : unsentMessages) {
            jdbcMessageRepository.markAsSent(unsentMessage.getId());
        }

        unsentMessages = jdbcMessageRepository.findUnsentMessages(10);
        assertThat(unsentMessages).hasSize(5);
        clientIds = unsentMessages.stream().map(PersistedMessage::getMessage).map(Message::getClientId).collect(Collectors.toList());
        assertThat(clientIds).containsExactly("5", "6", "7", "8", "9");
    }

}

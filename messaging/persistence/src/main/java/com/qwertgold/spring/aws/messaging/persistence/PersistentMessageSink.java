package com.qwertgold.spring.aws.messaging.persistence;

import com.qwertgold.spring.aws.messaging.core.domain.Message;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSink;
import com.qwertgold.spring.aws.messaging.persistence.spi.MessageRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class PersistentMessageSink implements MessageSink {

    private final MessageSink delegate;
    private final MessageRepository messageRepository;

    @Override
    public void receive(Message message) {
        String id = messageRepository.storeMessage(message);
        forwardAndMarkAsSent(message, id);
    }

    protected void forwardAndMarkAsSent(Message message, String id) {
        delegate.receive(message);
        messageRepository.markAsSent(id);
    }
}

package com.qwertgold.spring.aws.messaging.test;

import com.qwertgold.spring.aws.messaging.core.domain.Message;
import com.qwertgold.spring.aws.messaging.core.spi.MessageSink;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TestMessageSink implements MessageSink {

    private final List<Message> messages = new ArrayList<>();

    @Override
    public void receive(Message message) {
        messages.add(message);
    }

    public void clear() {
        messages.clear();
    }
}

package com.hellopublic.spring.aws.messaging.test;

import com.hellopublic.spring.aws.messaging.core.domain.Message;
import com.hellopublic.spring.aws.messaging.core.spi.MessageRouter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TestMessageRouter implements MessageRouter {

    private final List<Message> messages = new ArrayList<>();

    @Override
    public void route(Message message) {
        messages.add(message);
    }

    public void clear() {
        messages.clear();
    }
}

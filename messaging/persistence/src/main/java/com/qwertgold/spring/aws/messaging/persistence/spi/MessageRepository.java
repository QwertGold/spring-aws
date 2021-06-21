package com.qwertgold.spring.aws.messaging.persistence.spi;

import com.qwertgold.spring.aws.messaging.core.domain.Message;
import com.qwertgold.spring.aws.messaging.persistence.dao.PersistedMessage;

import java.util.List;

public interface MessageRepository {

    String storeMessage(Message message);

    List<PersistedMessage> findUnsentMessages(int maxResults);

    void markAsSent(String id);
}

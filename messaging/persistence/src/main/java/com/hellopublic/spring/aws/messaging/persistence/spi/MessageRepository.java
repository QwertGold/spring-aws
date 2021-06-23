package com.hellopublic.spring.aws.messaging.persistence.spi;

import com.hellopublic.spring.aws.messaging.core.domain.Message;
import com.hellopublic.spring.aws.messaging.persistence.dao.PersistedMessage;

import java.util.List;

public interface MessageRepository {

    String storeMessage(Message message);

    List<PersistedMessage> findUnsentMessages(int maxResults);

    void markAsSent(String id);
}

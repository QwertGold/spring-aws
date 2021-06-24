package com.hellopublic.spring.aws.messaging.persistence.spi;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.domain.Message;
import com.hellopublic.spring.aws.messaging.persistence.dao.PersistedMessage;

import java.util.List;
import java.util.Set;

public interface MessageRepository {

    String storeMessage(Message message, Destination destination);

    List<PersistedMessage> findUnsentMessages(int maxResults);

    void markAsSent(String id);
}

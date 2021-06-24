package com.hellopublic.spring.aws.messaging.persistence.customization;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.Message;
import com.hellopublic.spring.aws.messaging.persistence.dao.PersistedMessage;

import java.util.List;

/**
 * A message repository abstraction, which can store messages as PersistedMessage, and read them back.
 */
public interface MessageRepository {

    /**
     * Store a message and return the generated ID
     * @param message the message to store
     * @param destination the destination for the message
     * @return the ID identifying this row in the data store
     */
    String storeMessage(Message message, Destination destination);

    /**
     * Find messages that needs to be resent. This is used be the resend mechanism
     * @param maxResults the maximum number of rows to load
     * @return a List of PersistedMessage with data from the data store
     */
    List<PersistedMessage> findMessagesToResend(int maxResults);

    long countAllUnsentMessages();

    /**
     * Marks a message as successfully sent
     * @param id the ID of the row which was sent
     */
    void markAsSent(String id);

    /**
     * Callback if sending fails
     * @param message the message that could not be sent
     * @param id te ID of the row
     */
    void resendFailed(Message message, String id);
}

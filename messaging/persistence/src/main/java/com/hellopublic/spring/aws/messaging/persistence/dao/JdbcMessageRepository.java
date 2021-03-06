package com.hellopublic.spring.aws.messaging.persistence.dao;

import com.google.common.base.Preconditions;
import com.hellopublic.spring.aws.messaging.core.customization.JsonConverter;
import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.Message;
import com.hellopublic.spring.aws.messaging.core.util.IdGenerator;
import com.hellopublic.spring.aws.messaging.persistence.customization.MessageRepository;
import com.hellopublic.spring.aws.messaging.persistence.customization.ResendCalculator;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.TestOnly;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Repository implementation which uses Spring JDBC Template
 */
@Repository
@RequiredArgsConstructor
public class JdbcMessageRepository implements MessageRepository {


    public static final String UNSENT = "UNSENT";
    public static final String SENT = "SENT";

    private final ObjectProvider<JdbcTemplate> jdbcTemplateProvider;
    private final ResendCalculator resendCalculator;
    private final JsonConverter jsonConverter;
    private final QueryBuilder queryBuilder;
    private JdbcTemplate jdbcTemplate;


    @PostConstruct
    public void checkDependenciesAndCreateQueries() {
        jdbcTemplate = jdbcTemplateProvider.getIfAvailable();
        Preconditions.checkNotNull(jdbcTemplate, "Unable to find a JdbcTemplate, this bean is required for persistence dependency.");
    }

    @Override
    public String storeMessage(Message message, Destination destination) {

        String id = IdGenerator.generateId();

        jdbcTemplate.update(queryBuilder.getInsertQuery(),
                id,
                jsonConverter.toJson(message.getPayload()),
                destination.getTarget(),
                destination.getType(),
                jsonConverter.toJson(message.getHeaders()),
                Timestamp.from(Instant.now()),
                Timestamp.from(resendCalculator.calculateNextSend(message)),
                message.getPayload().getClass().getName(),
                UNSENT,
                message.getClientId()
        );
        return id;
    }

    @Override
    public List<PersistedMessage> findMessagesToResend(int maxResults) {
        return jdbcTemplate.query(queryBuilder.getSelectMessagesToResend(),
                new ArgumentPreparedStatementSetterWithLimit(maxResults, JdbcMessageRepository.UNSENT, Timestamp.from(Instant.now())),
                new PersistedMessageRowMapper());
    }

    @TestOnly
    public List<PersistedMessage> findAllMessages(int maxResults) {
        return jdbcTemplate.query(queryBuilder.getSelectAllMessages(), new ArgumentPreparedStatementSetterWithLimit(maxResults),
                new PersistedMessageRowMapper());
    }

    @Override
    public long countAllUnsentMessages() {
        Long count = jdbcTemplate.queryForObject(queryBuilder.getSelectCountQuery(), Long.class, UNSENT);
        if (count == null) {
            return 0L;
        }
        return count;
    }


    private Message createMessage(String payloadJson, String clazz, String headers, String clientId) {
        Object payload = jsonConverter.fromJsonByClassName(payloadJson, clazz);
        return new Message()
                .setPayload(payload)
                .setHeaders(jsonConverter.readHeaders(headers))
                .setClientId(clientId)
                ;
    }

    @Override
    public void markAsSent(String id) {
        jdbcTemplate.update(queryBuilder.getUpdateStatusQuery(), SENT, id);
    }

    @Override
    public void resendFailed(Message message, String id) {
        jdbcTemplate.update(queryBuilder.getUpdateNextSendQuery(), Timestamp.from(resendCalculator.calculateNextSend(message)), id);
    }

    @TestOnly
    public void deleteAll() {
        jdbcTemplate.update("truncate message");
    }

    private class PersistedMessageRowMapper implements RowMapper<PersistedMessage> {
        @Override
        public PersistedMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
            String payload = rs.getString(2);
            String destinationName = rs.getString(3);
            String destinationType = rs.getString(4);
            String headers = rs.getString(5);
            String clazz = rs.getString(8);
            String clientId = rs.getString(10);

            return new PersistedMessage()
                    .setId(rs.getString(1))
                    .setMessage(createMessage(payload, clazz, headers, clientId))
                    .setDestination(new Destination(destinationName, destinationType))
                    .setCreated(rs.getTimestamp(6).toInstant())
                    .setNextSend(rs.getTimestamp(7).toInstant())
                    .setStatus(rs.getString(9))
                    ;

        }
    }
}

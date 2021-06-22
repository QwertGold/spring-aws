package com.qwertgold.spring.aws.messaging.persistence.dao;

import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.domain.Message;
import com.qwertgold.spring.aws.messaging.core.spi.JsonConverter;
import com.qwertgold.spring.aws.messaging.core.util.IdGenerator;
import com.qwertgold.spring.aws.messaging.persistence.spi.MessageRepository;
import com.qwertgold.spring.aws.messaging.persistence.spi.ResendCalculator;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.TestOnly;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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

    private static final String UPDATE_STATUS = "update message set status = ? where id = ?";
    private static final String INSERT_QUERY = "insert into message " +
            "(id, payload, destination, " +
            "destination_type, attributes, created_at, " +
            "next_send, class, status, client_id) " +
            "values " +
            "(?, ?, ?," +
            " ?, ?, ?," +
            " ?, ?, ?, ?)";
    public static final String UNSENT = "UNSENT";
    public static final String SENT = "SENT";

    private final JdbcTemplate jdbcTemplate;
    private final ResendCalculator resendCalculator;
    private final JsonConverter jsonConverter;


    @Override
    public String storeMessage(Message message) {

        String id = IdGenerator.generateId();

        jdbcTemplate.update(INSERT_QUERY,
                id,
                jsonConverter.toJson(message.getPayload()),
                message.getDestination().getDestination(),
                message.getDestination().getDestinationType(),
                jsonConverter.toJson(message.getHeaders()),
                Timestamp.from(Instant.now()),
                resendCalculator.calculateNextSend(message),
                message.getPayload().getClass().getName(),
                UNSENT,
                message.getClientId()
        );
        return id;
    }

    @Override
    public List<PersistedMessage> findUnsentMessages(int maxResults) {
        return jdbcTemplate.query("select * from message where status = ? order by created_at",
                new ArgumentPreparedStatementSetterWithLimit(maxResults, JdbcMessageRepository.UNSENT),
                new PersistedMessageRowMapper());
    }


    @TestOnly
    public List<PersistedMessage> findAllMessages(int maxResults) {
        return jdbcTemplate.query("select * from message order by created_at", new ArgumentPreparedStatementSetterWithLimit(maxResults),
                new PersistedMessageRowMapper());
    }

    private Message createMessage(String payloadJson, String clazz, String destinationName, String destinationType, String headers, String clientId) {
        Object payload = jsonConverter.fromJsonByClassName(payloadJson, clazz);
        return new Message()
                .setPayload(payload)
                .setDestination(new Destination(destinationName, destinationType))
                .setHeaders(jsonConverter.readHeaders(headers))
                .setClientId(clientId)
                ;
    }

    @Override
    public void markAsSent(String id) {
        jdbcTemplate.update(UPDATE_STATUS, SENT, id);
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
                    .setMessage(createMessage(payload, clazz, destinationName, destinationType, headers, clientId))
                    .setCreated(rs.getTimestamp(6).toInstant())
                    .setNextSend(rs.getTimestamp(7).toInstant())
                    .setClazz(clazz)
                    .setStatus(rs.getString(9))
                    ;

        }
    }
}

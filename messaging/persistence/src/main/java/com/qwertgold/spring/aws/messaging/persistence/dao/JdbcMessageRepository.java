package com.qwertgold.spring.aws.messaging.persistence.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.domain.Header;
import com.qwertgold.spring.aws.messaging.core.domain.Message;
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
import java.util.Map;

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

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final MapType headerMapType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Header.class);

    @Override
    public String storeMessage(Message message) {

        String id = IdGenerator.generateId();
        try {
            jdbcTemplate.update(INSERT_QUERY,
                    id,
                    objectMapper.writeValueAsString(message.getPayload()),
                    message.getDestination().getDestination(),
                    message.getDestination().getDestinationType(),
                    objectMapper.writeValueAsString(message.getHeaders()),
                    Timestamp.from(Instant.now()),
                    resendCalculator.calculateNextSend(message),
                    message.getPayload().getClass().getName(),
                    UNSENT,
                    message.getClientId()

            );
            return id;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to store message", e);
        }

    }

    @Override
    public List<PersistedMessage> findUnsentMessages(int maxResults) {
        // todo add limit
        return jdbcTemplate.query("select * from message where status = ?", new PersistedMessageRowMapper(), UNSENT);
    }


    @TestOnly
    public List<PersistedMessage> findAllMessages(int maxResults) {
        // todo add limit
        return jdbcTemplate.query("select * from message", new PersistedMessageRowMapper());
    }

    private Message createMessage(String payloadJson, String clazz, String destinationName, String destinationType, String headers, String clientId) throws ClassNotFoundException, JsonProcessingException {
        Object payload = objectMapper.readValue(payloadJson, Class.forName(clazz));
        return new Message()
                .setPayload(payload)
                .setDestination(new Destination(destinationName, destinationType))
                .setHeaders(objectMapper.readValue(headers, headerMapType))
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
            try {
                return new PersistedMessage()
                        .setId(rs.getString(1))
                        .setMessage(createMessage(payload, clazz, destinationName, destinationType, headers, clientId))
                        .setCreated(rs.getTimestamp(6).toInstant())
                        .setNextSend(rs.getTimestamp(7).toInstant())
                        .setClazz(clazz)
                        .setStatus(rs.getString(9))
                        ;
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Unable to find class " + clazz, e);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Unable to deserialize payload or headers", e);
            }
        }
    }
}

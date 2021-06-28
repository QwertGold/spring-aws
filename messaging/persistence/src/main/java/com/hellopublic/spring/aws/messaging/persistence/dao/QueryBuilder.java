package com.hellopublic.spring.aws.messaging.persistence.dao;

import com.hellopublic.spring.aws.messaging.persistence.PersistenceConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Helper class for JdbcMessageRepository, so we don't mix trivial query building with storage logic
 */
@Getter
@Component
@RequiredArgsConstructor
public class QueryBuilder {

    private static final String UPDATE_STATUS_FORMAT = "update %s set status = ? where id = ?";
    private static final String UPDATE_NEXT_SEND_FORMAT = "update %s set next_send = ? where id = ?";
    private static final String INSERT_QUERY_FORMAT = "insert into %s " +
            "(id, payload, destination, " +
            "destination_type, attributes, created_at, " +
            "next_send, class, status, client_id) " +
            "values " +
            "(?, ?, ?," +
            " ?, ?, ?," +
            " ?, ?, ?, ?)";

    private static final String SELECT_MESSAGES_TO_RESEND_FORMAT = "select * from %s where status = ? and next_send < ? order by next_send";
    private static final String SELECT_ALL_MESSAGES_FORMAT = "select * from %s order by created_at";
    private static final String SELECT_COUNT_UNSENT_FORMAT = "select count(*) from %s where status = ?";


    private final PersistenceConfiguration persistenceConfiguration;
    private String updateStatusQuery;
    private String updateNextSendQuery;
    private String selectMessagesToResend;
    private String selectAllMessages;
    private String insertQuery;
    private String selectCountQuery;

    @PostConstruct
    public void buildQueries() {
        updateStatusQuery = String.format(UPDATE_STATUS_FORMAT, persistenceConfiguration.getTableName());
        updateNextSendQuery = String.format(UPDATE_NEXT_SEND_FORMAT, persistenceConfiguration.getTableName());
        insertQuery = String.format(INSERT_QUERY_FORMAT, persistenceConfiguration.getTableName());
        selectMessagesToResend = String.format(SELECT_MESSAGES_TO_RESEND_FORMAT, persistenceConfiguration.getTableName());
        selectAllMessages = String.format(SELECT_ALL_MESSAGES_FORMAT, persistenceConfiguration.getTableName());
        selectCountQuery = String.format(SELECT_COUNT_UNSENT_FORMAT, persistenceConfiguration.getTableName());
    }
}

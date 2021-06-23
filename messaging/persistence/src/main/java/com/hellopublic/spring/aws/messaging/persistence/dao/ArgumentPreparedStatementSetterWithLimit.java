package com.hellopublic.spring.aws.messaging.persistence.dao;

import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * An extension of the normal ArgumentPreparedStatementSetter so we can use the JDBC drivers maxRows. This is to avoid another abstraction to support the
 * database specific maxRow keyword  (limit/top/rownum)
 */
class ArgumentPreparedStatementSetterWithLimit extends ArgumentPreparedStatementSetter {
    private final int maxRows;

    public ArgumentPreparedStatementSetterWithLimit(int maxRows, Object... objects) {
        super(objects);
        this.maxRows = maxRows;
    }

    @Override
    protected void doSetValue(PreparedStatement ps, int parameterPosition, Object argValue) throws SQLException {
        ps.setMaxRows(maxRows);
        super.doSetValue(ps, parameterPosition, argValue);
    }
}

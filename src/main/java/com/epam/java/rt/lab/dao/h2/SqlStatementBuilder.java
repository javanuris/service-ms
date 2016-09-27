package com.epam.java.rt.lab.dao.h2;

/**
 *
 */
public class SqlStatementBuilder {

    private enum StatementType {
        INSERT_INTO,
        SELECT,
        UPDATE,
        DELETE_FROM
    }

    StatementType type;

    private SqlStatementBuilder(StatementType type) {
        this.type = type;
    }

    public static SqlStatementBuilder insert() {
        return new SqlStatementBuilder(StatementType.INSERT_INTO);
    }

    public static SqlStatementBuilder select() {
        return new SqlStatementBuilder(StatementType.SELECT);
    }

    public static SqlStatementBuilder update() {
        return new SqlStatementBuilder(StatementType.UPDATE);
    }

    public static SqlStatementBuilder delete() {
        return new SqlStatementBuilder(StatementType.DELETE_FROM);
    }



}

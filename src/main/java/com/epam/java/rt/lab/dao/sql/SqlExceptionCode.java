package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.exception.ExceptionCode;

public enum SqlExceptionCode implements ExceptionCode {

    SQL_TABLE_NAME_NOT_FOUND(0),
    PROPERTY_NOT_ASSIGNABLE_TO_TABLE(1),
    PREDICATE_ONLY_EQUAL_ALLOWED(2),
    PREDICATE_ONLY_AND_OR_ALLOWED(3),
    PREDICATE_SPECIAL_ALLOWED(4),
    ENTITY_PROPERTY_NOT_FROM_ENTITY(5);

    private final int number;

    SqlExceptionCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

}

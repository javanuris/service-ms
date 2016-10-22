package com.epam.java.rt.lab.dao;

import com.epam.java.rt.lab.exception.ExceptionCode;

public enum DaoExceptionCode implements ExceptionCode {

    DB_DRIVER_CLASS_NOT_FOUND(0),
    DB_FACTORY_CLASS_NOT_FOUND(1),
    DB_FACTORY_CONSTRUCTOR_NOT_FOUND(2),
    GET_CONNECTION_ERROR(3),
    DB_FACTORY_INSTANTIATE_ERROR(4),
    BEGIN_TRANSACTION_ERROR(5),
    COMMIT_TRANSACTION_ERROR(6),
    ROLLBACK_TRANSACTION_ERROR(7),
    CLOSE_CONNECTION_ERROR(8),
    DAO_CLASS_INSTANTIATE_ERROR(9),
    DAO_CLASS_NOT_FOUND(10),
    DAO_CONSTRUCTOR_NOT_FOUND(11),
    DAO_STATEMENT_NOT_FOUND(12),
    DAO_STATEMENT_METHOD_NOT_FOUND(13),
    DAO_STATEMENT_METHOD_CALL_ERROR(14),
    DAO_STATEMENT_ERROR(15),
    DAO_PROPERTY_FILE_URI_ERROR(16),
    UPDATE_ERROR(17),
    SQL_OPERATION_ERROR(18),
    AUTO_CLOSABLE_OBJECT_ERROR(19),
    ENTITY_CLASS_NOT_FOUND(20),
    VALUE_TYPE_NOT_FOUND(21),
    ENTITY_SETTER_METHOD_NOT_FOUND(22),
    ENTITY_SETTER_METHOD_INVOKE_ERROR(23);

    private final int number;

    DaoExceptionCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

}

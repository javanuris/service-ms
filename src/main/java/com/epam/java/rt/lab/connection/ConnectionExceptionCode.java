package com.epam.java.rt.lab.connection;

import com.epam.java.rt.lab.exception.ExceptionCode;

public enum ConnectionExceptionCode implements ExceptionCode {

    RELEASE_CONNECTION_TIMEOUT_ERROR(0),
    SHUTDOWN_TIMEOUT_ERROR(1);

    private final int number;

    ConnectionExceptionCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

}

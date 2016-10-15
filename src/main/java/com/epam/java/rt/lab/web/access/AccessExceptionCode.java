package com.epam.java.rt.lab.web.access;

import com.epam.java.rt.lab.exception.ExceptionCode;

public enum AccessExceptionCode implements ExceptionCode {

    PROPERTY_READ_ERROR(0),
    PROPERTY_EMPTY_OR_CONTENT_ERROR(1),
    ROLE_NOT_FOUND(2);

    private final int number;

    AccessExceptionCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

}



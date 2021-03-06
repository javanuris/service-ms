package com.epam.java.rt.lab.web.access;

import com.epam.java.rt.lab.exception.ExceptionCode;

public enum AccessExceptionCode implements ExceptionCode {

    ROLE_NOT_FOUND(0);

    private final int number;

    AccessExceptionCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

}



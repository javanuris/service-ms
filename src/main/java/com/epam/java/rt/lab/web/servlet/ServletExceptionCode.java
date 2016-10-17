package com.epam.java.rt.lab.web.servlet;

import com.epam.java.rt.lab.exception.ExceptionCode;

public enum ServletExceptionCode implements ExceptionCode {

    CONTENT_LENGTH_HEADER_ERROR(0),
    CONTENT_LENGTH_ERROR(1);

    private final int number;

    ServletExceptionCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

}



package com.epam.java.rt.lab.util;

import com.epam.java.rt.lab.exception.ExceptionCode;

enum UtilExceptionCode implements ExceptionCode {

    PROPERTY_READ_ERROR(0);

    private final int number;

    UtilExceptionCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

}

package com.epam.java.rt.lab.exception;

public enum AppExceptionCode implements ExceptionCode {

    NULL_EXCEPTION(0),
    NULL_NOT_ALLOWED(1),
    PROPERTY_READ_ERROR(2),
    PROPERTY_EMPTY_OR_CONTENT_ERROR(3);

    private final int number;

    AppExceptionCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

}

package com.epam.java.rt.lab.exception;

enum AppExceptionCode implements ExceptionCode {

    NULL_EXCEPTION(0),
    NULL_NOT_ALLOWED(1);

    private final int number;

    AppExceptionCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

}

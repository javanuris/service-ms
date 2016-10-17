package com.epam.java.rt.lab.web.validator;

import com.epam.java.rt.lab.exception.ExceptionCode;

enum ValidatorExceptionCode implements ExceptionCode {

    CLASS_OR_METHOD_NOT_FOUND(0),
    METHOD_INVOKE_ERROR(1),
    NOT_NUMBER_CLASS_ERROR(2),
    NOT_TIMESTAMP_VALUE_ERROR(3),
    VALIDATOR_NOT_FOUND(4);

    private final int number;

    ValidatorExceptionCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

}

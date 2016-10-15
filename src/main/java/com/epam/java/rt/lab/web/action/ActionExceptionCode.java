package com.epam.java.rt.lab.web.action;

import com.epam.java.rt.lab.exception.ExceptionCode;

public enum ActionExceptionCode implements ExceptionCode {

    ACTION_OBJECT_INSTANTIATE_ERROR(0),
    ACTION_MAP_EMPTY(1),
    ACTION_BY_URI_NOT_FOUND(2),
    ACTION_FORWARD_TO_JSP_ERROR(3);

    private final int number;

    ActionExceptionCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

}

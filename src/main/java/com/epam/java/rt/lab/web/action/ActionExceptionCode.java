package com.epam.java.rt.lab.web.action;

import com.epam.java.rt.lab.exception.ExceptionCode;

public enum ActionExceptionCode implements ExceptionCode {

    ACTION_OBJECT_INSTANTIATE_ERROR(0),
    ACTION_CLASS_NOT_FOUND(1),
    ACTION_MAP_EMPTY(2),
    ACTION_BY_URI_NOT_FOUND(3),
    ACTION_FORWARD_TO_JSP_ERROR(4),
    ACTION_INSTANTIATE_SERVICE_ERROR(5);

    private final int number;

    ActionExceptionCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

}

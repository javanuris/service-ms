package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.exception.ExceptionCode;

public enum ServiceExceptionCode implements ExceptionCode {

    CREATE_DAO_FACTORY_OR_DAO_ERROR(0),
    DAO_CLOSE_ERROR(1),
    ADD_REMEMBER_ERROR(2),
    REMOVE_REMEMBER_ERROR(3),
    GET_REMEMBER_ERROR(4),
    GET_USER_ERROR(5),
    AVATAR_FILE_ACCESS_ERROR(6);

    private final int number;

    ServiceExceptionCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

}

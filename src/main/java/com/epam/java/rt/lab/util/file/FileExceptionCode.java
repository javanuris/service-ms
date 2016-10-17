package com.epam.java.rt.lab.util.file;

import com.epam.java.rt.lab.exception.ExceptionCode;

enum FileExceptionCode implements ExceptionCode {

    FILE_ACCESS_ERROR(0),
    UPLOAD_TYPE_ERROR(1),
    CONTENT_TYPE_ERROR(2),
    FILE_NOT_FOUND(3);

    private final int number;

    FileExceptionCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

}

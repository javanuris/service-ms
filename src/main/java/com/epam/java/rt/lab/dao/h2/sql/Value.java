package com.epam.java.rt.lab.dao.H2.sql;

/**
 * service-ms
 */
public class Value<T> {
    private T value;

    public Value(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

}

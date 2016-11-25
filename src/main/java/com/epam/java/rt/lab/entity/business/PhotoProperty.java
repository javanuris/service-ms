package com.epam.java.rt.lab.entity.business;

import com.epam.java.rt.lab.entity.EntityProperty;

public enum PhotoProperty implements EntityProperty {
    ID,
    NAME,
    TYPE,
    FILE,
    MODIFIED;

    @Override
    public Class getEntityClass() {
        return Photo.class;
    }
}

package com.epam.java.rt.lab.entity.access;

import com.epam.java.rt.lab.entity.EntityProperty;

public enum LoginProperty implements EntityProperty {
    ID,
    EMAIL,
    SALT,
    PASSWORD,
    ATTEMPT_LEFT,
    STATUS;

    @Override
    public Class getEntityClass() {
        return Login.class;
    }
}

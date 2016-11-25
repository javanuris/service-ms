package com.epam.java.rt.lab.entity.access;

import com.epam.java.rt.lab.entity.EntityProperty;

public enum RememberProperty implements EntityProperty {
    ID,
    USER_ID,
    COOKIE_NAME,
    COOKIE_VALUE,
    VALID;

    @Override
    public Class getEntityClass() {
        return Remember.class;
    }
}

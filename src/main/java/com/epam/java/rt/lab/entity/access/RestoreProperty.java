package com.epam.java.rt.lab.entity.access;

import com.epam.java.rt.lab.entity.EntityProperty;

public enum RestoreProperty implements EntityProperty {
    ID,
    LOGIN_ID,
    CODE,
    COOKIE_NAME,
    COOKIE_VALUE,
    VALID;

    @Override
    public Class getEntityClass() {
        return Restore.class;
    }
}

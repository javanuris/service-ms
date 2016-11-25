package com.epam.java.rt.lab.entity.access;

import com.epam.java.rt.lab.entity.EntityProperty;

public enum ActivateProperty implements EntityProperty {
    ID,
    EMAIL,
    SALT,
    PASSWORD,
    CODE,
    VALID;

    @Override
    public Class getEntityClass() {
        return Activate.class;
    }
}

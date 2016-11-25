package com.epam.java.rt.lab.entity.business;

import com.epam.java.rt.lab.entity.EntityProperty;

public enum ApplicationProperty implements EntityProperty {
    ID,
    USER_ID,
    CREATED,
    CATEGORY_ID,
    MESSAGE;

    @Override
    public Class getEntityClass() {
        return Application.class;
    }
}

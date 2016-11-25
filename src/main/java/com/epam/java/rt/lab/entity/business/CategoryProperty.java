package com.epam.java.rt.lab.entity.business;

import com.epam.java.rt.lab.entity.EntityProperty;

public enum CategoryProperty implements EntityProperty {
    ID,
    PARENT_ID,
    CREATED,
    NAME;

    @Override
    public Class getEntityClass() {
        return Category.class;
    }
}

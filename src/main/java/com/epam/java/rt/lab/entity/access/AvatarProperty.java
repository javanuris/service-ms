package com.epam.java.rt.lab.entity.access;

import com.epam.java.rt.lab.entity.EntityProperty;

public enum AvatarProperty implements EntityProperty {
    ID,
    NAME,
    TYPE,
    FILE,
    MODIFIED;

    @Override
    public Class getEntityClass() {
        return Avatar.class;
    }
}

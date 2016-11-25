package com.epam.java.rt.lab.entity.access;

import com.epam.java.rt.lab.entity.EntityProperty;

public enum UserProperty implements EntityProperty {
    ID,
    FIRST_NAME,
    MIDDLE_NAME,
    LAST_NAME,
    LOGIN_ID,
    ROLE_NAME,
    AVATAR_ID;

    @Override
    public Class getEntityClass() {
        return User.class;
    }
}

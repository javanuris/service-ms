package com.epam.java.rt.lab.entity.business;

import com.epam.java.rt.lab.entity.EntityProperty;

public enum CommentProperty implements EntityProperty {
    ID,
    USER_ID,
    CREATED,
    APPLICATION_ID,
    PHOTO_ID,
    MESSAGE;

    @Override
    public Class getEntityClass() {
        return Comment.class;
    }
}

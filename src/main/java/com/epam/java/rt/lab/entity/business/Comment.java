package com.epam.java.rt.lab.entity.business;

import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.entity.rbac.User;

import java.sql.Timestamp;

/**
 * category-ms
 */
public class Comment extends BaseEntity {

    private User user;
    private Timestamp created;
    private Long applicationId;
    private Long photoId;
    private String message;

    public enum Property implements EntityProperty {
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

    public Comment() {
    }


    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

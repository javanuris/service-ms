package com.epam.java.rt.lab.entity.business;

import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.entity.rbac.User;

import java.sql.Timestamp;

/**
 * category-ms
 */
public class Application extends BaseEntity {
    private User user;
    private Timestamp created;
    private Category category;
    private String message;

    public enum Property implements EntityProperty {
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

    public Application() {
    }

    public Application(Long id, User user, Timestamp created, Category category, String message) {
        super(id);
        this.user = user;
        this.created = created;
        this.category = category;
        this.message = message;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

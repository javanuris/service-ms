package com.epam.java.rt.lab.entity.business;

import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.entity.access.User;

import java.sql.Timestamp;

public class Application extends BaseEntity {

    private User user;
    private Timestamp created;
    private Category category;
    private String message;

    public static final Application NULL_APPLICATION = new Application();

    public Application() {
    }

    public Application(Long id, User user, Timestamp created,
                       Category category, String message) {
        super(id);
        this.user = user;
        this.created = created;
        this.category = category;
        this.message = message;
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

package com.epam.java.rt.lab.entity.access;

import com.epam.java.rt.lab.entity.BaseEntity;

import java.sql.Timestamp;

public class Remember extends BaseEntity {
    private User user;
    private String cookieName;
    private String cookieValue;
    private Timestamp valid;

    public Remember() {
    }

    public Remember(Long id, User user, String cookieName,
                    String cookieValue, Timestamp valid) {
        super(id);
        this.user = user;
        this.cookieName = cookieName;
        this.cookieValue = cookieValue;
        this.valid = valid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getCookieValue() {
        return cookieValue;
    }

    public void setCookieValue(String cookieValue) {
        this.cookieValue = cookieValue;
    }

    public Timestamp getValid() {
        return valid;
    }

    public void setValid(Timestamp valid) {
        this.valid = valid;
    }
}

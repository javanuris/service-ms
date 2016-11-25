package com.epam.java.rt.lab.entity.access;

import com.epam.java.rt.lab.entity.BaseEntity;

import java.sql.Timestamp;

public class Restore extends BaseEntity {
    private Login login;
    private String code;
    private String cookieName;
    private String cookieValue;
    private Timestamp valid;

    public Restore() {
    }

    public Restore(Long id, Login login, String code, String cookieName,
                   String cookieValue, Timestamp valid) {
        super(id);
        this.login = login;
        this.code = code;
        this.cookieName = cookieName;
        this.cookieValue = cookieValue;
        this.valid = valid;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

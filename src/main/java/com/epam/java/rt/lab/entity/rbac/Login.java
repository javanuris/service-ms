package com.epam.java.rt.lab.entity.rbac;

import com.epam.java.rt.lab.entity.BaseEntity;

/**
 * service-ms
 */
public class Login extends BaseEntity {
    private String email;
    private String password;
    private int attemptLeft;
    private boolean active;

    public Login() {
    }

    public Login(Long id, String email, String password, int attemptLeft, boolean active) {
        super(id);
        this.email = email;
        this.password = password;
        this.attemptLeft = attemptLeft;
        this.active = active;
    }

    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAttemptLeft() {
        return attemptLeft;
    }

    public void setAttemptLeft(int attemptLeft) {
        this.attemptLeft = attemptLeft;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

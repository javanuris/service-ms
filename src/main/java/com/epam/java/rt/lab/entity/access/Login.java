package com.epam.java.rt.lab.entity.access;

import com.epam.java.rt.lab.entity.BaseEntity;

public class Login extends BaseEntity {

    private String email;
    private String salt;
    private String password;
    private int attemptLeft;
    private int status;

    public static final Login NULL_LOGIN = new Login();

    public Login() {
    }

    public Login(Long id, String email, String salt, String password,
                 int attemptLeft, int status) {
        super(id);
        this.email = email;
        this.salt = salt;
        this.password = password;
        this.attemptLeft = attemptLeft;
        this.status = status;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

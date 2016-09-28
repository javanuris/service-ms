package com.epam.java.rt.lab.entity.rbac;

import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.entity.BaseEntity;

/**
 * service-ms
 */
public class Login extends BaseEntity {

    public enum Property implements EntityProperty {
        ID,
        EMAIL,
        PASSWORD,
        ATTEMPT_LEFT,
        STATUS;

        @Override
        public Class getEntityClass() {
            return Login.class;
        }
    }

    private String email;
    private String password;
    private int attemptLeft;
    private int status;

    public Login() {
    }

    public Login(Long id, String email, String password, int attemptLeft, int status) {
        super(id);
        this.email = email;
        this.password = password;
        this.attemptLeft = attemptLeft;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
